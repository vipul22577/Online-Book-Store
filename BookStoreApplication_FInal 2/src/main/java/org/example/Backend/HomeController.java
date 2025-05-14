package org.example.Backend;
import static org.example.Frontend.BookStoreController.currentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Platform;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HomeController {
    @FXML
    public Button bt1Cart;
    @FXML
    public Button bt2Cart;
    @FXML
    public Button bt3Cart;
    @FXML
    public Button myCart;
    @FXML
    public Button logout;
    @FXML
    public Label bt1ISBN;
    @FXML
    public Label bt2ISBN;
    @FXML
    public Label bt3ISBN;
    @FXML
    public Label avail1;
    @FXML
    public Label avail2;
    @FXML
    public Label avail3;
    @FXML
    public Button rev1;
    @FXML
    public Button rev2;
    @FXML
    public Button rev3;

    public static ArrayList<String> booksSelected = new ArrayList<>();
    public final ArrayList<Button> buttonPressed = new ArrayList<>();

    public ObjectMapper objectMapper = new ObjectMapper();
    public boolean sendRequest(String isbn) throws IOException {
        String serverAddress = "localhost";
        int port = 243;

        try (Socket socket = new Socket(serverAddress, port);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            Map<String, String> requestMap = Map.of("action", "checkISBN", "isbn", isbn);
            String requestJson = objectMapper.writeValueAsString(requestMap);
            writer.write(requestJson);
            writer.newLine();
            writer.flush();
            String response = reader.readLine();
            System.out.println(response);
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            System.out.println(responseMap.get("status"));
            System.out.println("success".equals(responseMap.get("status")));
            return "success".equals(responseMap.get("status"));
        }
    }
    public void addToCart(ActionEvent e) throws IOException {
        Button clickedButton = (Button) e.getSource();
        if (!buttonPressed.contains(clickedButton)) {
            Label ISBNlabel = (Label) ((AnchorPane) clickedButton.getParent()).getChildren().get(6); // Check index
            String ISBN = ISBNlabel.getText();
            if (sendRequest(ISBN)) {
                Platform.runLater(() -> {
                    buttonPressed.add(clickedButton);
                    booksSelected.add(ISBN);
                });
            } else {
                Platform.runLater(() -> System.out.println("Request denied by server"));
            }
        }
    }

    public void myCartButtonOnAction(ActionEvent e) throws IOException {
        if (sendRequest("cart-view")) {
            System.out.println("Hello Cart view ");
            Platform.runLater(() -> {
                try {
                    System.out.println(booksSelected); // Assuming booksSelected is a variable holding selected books
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bookstoreapplication_final/cart.fxml"));
                    Scene scene = new Scene(loader.load(), 600, 400);
                    Stage currentStage = new Stage();
                    currentStage.initStyle(StageStyle.UNDECORATED);
                    currentStage.setScene(scene);
                    Stage stage = (Stage) myCart.getScene().getWindow();
                    stage.close();
                    currentStage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        } else {
            Platform.runLater(() -> System.out.println("Cart view request denied by server"));
        }
    }
    public void writeReview(ActionEvent e) throws SQLException, IOException {
        if (sendRequest("Review")) {
            Button clickedButton = (Button) e.getSource();
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter number of stars: ");
            String rev = sc.next();
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            connectDB.setAutoCommit(false);
            String review = "UPDATE book SET review = ? WHERE ISBN = ?";
            PreparedStatement stm = connectDB.prepareStatement(review);
            stm.setString(1, rev);
            if (clickedButton.equals(rev1)) {
                stm.setInt(2, 123456789);
            } else if (clickedButton.equals(rev2)) {
                stm.setInt(2, 234567890);
            } else if (clickedButton.equals(rev3)) {
                stm.setInt(2, 345678901);
            }
            stm.executeUpdate();
            connectDB.commit();
            connectDB.close();
        }
    }


    public void logoutButtonOnAction(ActionEvent e) throws IOException {
        if (sendRequest("logout")) {

            Platform.runLater(() -> {
                try {
                    currentUser = 0;  // Assume currentUser is a static variable indicating the logged-in user
                    booksSelected.clear(); // Assume booksSelected is a list storing selected books
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bookstoreapplication_final/login.fxml"));
                    Scene scene = new Scene(loader.load(), 520, 400);
                    Stage currentStage = new Stage();
                    currentStage.initStyle(StageStyle.UNDECORATED);
                    currentStage.setScene(scene);

                    Stage stage = (Stage) logout.getScene().getWindow();
                    stage.close();
                    currentStage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        } else {
            Platform.runLater(() -> System.out.println("Logout request denied by server"));
        }
    }

    public void refreshHandle(ActionEvent e) throws IOException {
        if (sendRequest("refresh-stock")) {
            Platform.runLater(() -> {
                // Assuming 'booksSelected' is a list that keeps track of which books are in the cart or selected.
                if (booksSelected.contains(bt1ISBN.getText())) {
                    avail1.setText("Out of Stock");
                    avail1.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                }
                if (booksSelected.contains(bt2ISBN.getText())) {
                    avail2.setText("Out of Stock");
                    avail2.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                }
                if (booksSelected.contains(bt3ISBN.getText())) {
                    avail3.setText("Out of Stock");
                    avail3.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                }
            });
        } else {
            Platform.runLater(() -> System.out.println("Refresh request denied by server"));
        }
    }
}
