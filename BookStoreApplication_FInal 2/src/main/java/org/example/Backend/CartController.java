package org.example.Backend;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.sql.*;
import java.time.LocalTime;
import static org.example.Frontend.BookStoreController.currentUser;
import static org.example.Backend.HomeController.booksSelected;
import java.io.*;

public class CartController {

    @FXML
    private Label user_id;
    @FXML
    private Label isbn1;
    @FXML
    private Label isbn2;
    @FXML
    private Label isbn3;
    @FXML
    private Label it1;
    @FXML
    private Label it2;
    @FXML
    private Label it3;
    @FXML
    private Label bt1;
    @FXML
    private Label bt2;
    @FXML
    private Label bt3;
    @FXML
    private Label a1;
    @FXML
    private Label a2;
    @FXML
    private Label a3;
    @FXML
    private Label total;
    @FXML
    private Label offer;
    @FXML
    private Button r1;
    @FXML
    private Button r2;
    @FXML
    private Button r3;
    @FXML
    private Label dateTime;
    @FXML
    public  Button saveCart;
    @FXML
    private Button payNow;

    public  DatabaseConnection connectNow = new DatabaseConnection();
    public  Connection connectDB = connectNow.getConnection();
    public static LocalTime lt;
    public static int total_ = 0;
    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            dateTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd   HH:mm:ss")));
        }
    };
    public void refreshButtonOnAction() throws SQLException, IOException, InterruptedException {
        if (!validateAction("refresh")) {
            System.out.println("Refresh action not validated.");
            return;
        }
        total_ = 0;
        timer.start();
        user_id.setText(String.valueOf(currentUser));
        if (booksSelected.size() == 1) {
            it1.setText("1");
            isbn1.setText(booksSelected.get(0));
            r1.setText("REMOVE");
            r1.setStyle("-fx-background-color: #445df9");
            String title_and_price = "SELECT title, price FROM book WHERE ISBN ='" + booksSelected.get(0) + "'";
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(title_and_price);
            while (resultSet.next()) {
                bt1.setText(resultSet.getString(0));
                a1.setText(resultSet.getString(1));
            }
            total_ += Integer.parseInt(a1.getText());
            it2.setText("");
            it3.setText("");
            isbn2.setText("");
            isbn3.setText("");
            bt2.setText("");
            bt3.setText("");
            a2.setText("");
            a3.setText("");
            r2.setText("");
            r2.setTextFill(new Color(0, 0, 0, 0));
            r3.setText("");
            r3.setTextFill(new Color(0, 0, 0, 0));
        } else if (booksSelected.size() == 2) {
            it1.setText("1");
            isbn1.setText(booksSelected.get(0));
            r1.setText("REMOVE");
            r1.setStyle("-fx-background-color: #445df9");
            String title_and_price = "SELECT title, price FROM book WHERE ISBN ='" + booksSelected.get(0) + "'";
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(title_and_price);
            while (resultSet.next()) {
                bt1.setText(resultSet.getString(1));
                a1.setText(resultSet.getString(2));
            }
            total_ += Integer.parseInt(a1.getText());
            it2.setText("2");
            isbn2.setText(booksSelected.get(1));
            r2.setText("REMOVE");
            r2.setStyle("-fx-background-color: #445df9");
            String title_and_price2 = "SELECT title, price FROM book WHERE ISBN ='" + booksSelected.get(1) + "'";
            Statement statement2 = connectDB.createStatement();
            ResultSet resultSet2 = statement2.executeQuery(title_and_price2);
            while (resultSet2.next()) {
                bt2.setText(resultSet2.getString(1));
                a2.setText(resultSet2.getString(2));
            }
            total_ += Integer.parseInt(a2.getText());
            it3.setText("");
            bt3.setText("");
            isbn3.setText("");
            a3.setText("");
            r3.setText("");
            r3.setTextFill(new Color(0, 0, 0, 0));
        } else if (booksSelected.size() == 3) {
            it1.setText("1");
            isbn1.setText(booksSelected.get(0));
            r1.setText("REMOVE");
            r1.setStyle("-fx-background-color: #445df9");
            String title_and_price = "SELECT title, price FROM book WHERE ISBN ='" + booksSelected.get(0) + "'";
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(title_and_price);
            while (resultSet.next()) {
                bt1.setText(resultSet.getString(1));
                a1.setText(resultSet.getString(2));
            }
            total_ += Integer.parseInt(a1.getText());
            it2.setText("2");
            isbn2.setText(booksSelected.get(1));
            r2.setText("REMOVE");
            r2.setStyle("-fx-background-color: #445df9");
            String title_and_price2 = "SELECT title, price FROM book WHERE ISBN ='" + booksSelected.get(1) + "'";
            Statement statement2 = connectDB.createStatement();
            ResultSet resultSet2 = statement2.executeQuery(title_and_price2);
            while (resultSet2.next()) {
                bt2.setText(resultSet2.getString(1));
                a2.setText(resultSet2.getString(2));
            }
            total_ += Integer.parseInt(a2.getText());
            it3.setText("3");
            isbn3.setText(booksSelected.get(2));
            r3.setText("REMOVE");
            r3.setStyle("-fx-background-color: #445df9");
            String title_and_price3 = "SELECT title, price FROM book WHERE ISBN ='" + booksSelected.get(2) + "'";
            Statement statement3 = connectDB.createStatement();
            ResultSet resultSet3 = statement3.executeQuery(title_and_price3);
            while (resultSet3.next()) {
                bt3.setText(resultSet3.getString(1));
                a3.setText(resultSet3.getString(2));
            }
            total_ += Integer.parseInt(a3.getText());
        }
        total.setText(String.valueOf(total_));
        String offers = "SELECT description FROM apply INNER JOIN offers ON apply.offer_id = offers.offer_id WHERE user_id = '" + currentUser + "'";
        Statement statement = connectDB.createStatement();
        ResultSet resultSet = statement.executeQuery(offers);
        StringBuilder offer_for_user = new StringBuilder();
        while (resultSet.next()) {
            offer_for_user.append(resultSet.getString(1));
            offer_for_user.append("\n");
        }
        offer.setText(String.valueOf(offer_for_user));

    }
    public void removeButtonOnAction(ActionEvent e) throws IOException, InterruptedException {
        if (!validateAction("remove")) {
            System.out.println("Remove action not validated.");
            return;
        }
        Button clickedButton = (Button) e.getSource();
        if (clickedButton.equals(r1)) {
            it1.setText("");
            bt1.setText("");
            isbn1.setText("");
            total_ -= Integer.parseInt(a1.getText());
            a1.setText("");
            r1.setText("");
            r1.setStyle("-fx-background-color: White");
            booksSelected.remove(0);
        } else if (clickedButton.equals(r2)) {
            it2.setText("");
            bt2.setText("");
            isbn2.setText("");
            total_ -= Integer.parseInt(a2.getText());
            a2.setText("");
            r2.setText("");
            r2.setStyle("-fx-background-color: White");
            booksSelected.remove(1);
        } else {
            it3.setText("");
            bt3.setText("");
            isbn3.setText("");
            total_ -= Integer.parseInt(a3.getText());
            a3.setText("");
            r3.setText("");
            r3.setStyle("-fx-background-color: White");
            booksSelected.remove(2);
        }
    }

//    public void saveCartAndPay(ActionEvent e) throws IOException, InterruptedException, SQLException {
//        if (!validateAction("pay")) {
//            System.out.println("Payment action not validated.");
//            return;
//        }
//        PreparedStatement statement = null;
//        try {
//
//            connectDB.setAutoCommit(false);
//
//            String updateCart = "INSERT INTO cart (user_id, date, time, num_items, total_amount) VALUES (?,?,?,?,?)";
//            statement = connectDB.prepareStatement(updateCart);
//            statement.setInt(1, currentUser);
//            statement.setDate(2, Date.valueOf(LocalDate.now()));
//            lt = LocalTime.now();
//            statement.setTime(3, Time.valueOf(lt));
//            statement.setInt(4, booksSelected.size());
//            statement.setInt(5, total_);
//            statement.executeUpdate();
//
//            String updateBook = "UPDATE book SET availability = 'Out of Stock', user_id = ?, date = ?, time = ? WHERE ISBN = ?";
//            for (String isbn : booksSelected) {
//                statement = connectDB.prepareStatement(updateBook);
//                statement.setInt(1, currentUser);
//                statement.setDate(2, Date.valueOf(LocalDate.now()));
//                statement.setTime(3, Time.valueOf(lt));
//                statement.setString(4, isbn);
//                statement.executeUpdate();
//            }
//            connectDB.commit();
//            if (e.getSource().equals(payNow)) {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bookstoreapplication_final/payment.fxml"));
//                Scene scene = new Scene(loader.load(), 600, 400);
//                Stage currentStage = new Stage();
//                currentStage.initStyle(StageStyle.UNDECORATED);
//                currentStage.setScene(scene);
//                Stage stage = (Stage) payNow.getScene().getWindow();
//                stage.close();
//                currentStage.show();
//            }
//        } catch (SQLException ex) {
//            System.out.println("Transaction failed: " + ex.getMessage());
//            ex.printStackTrace();
//            // Rollback transaction
//            if (connectDB != null) {
//                try {
//                    connectDB.rollback();
//                } catch (SQLException rollbackEx) {
//                    System.out.println("Failed to rollback: " + rollbackEx.getMessage());
//                }
//            }
//        } finally {
//            if (statement != null) {
//                statement.close();
//            }
//            connectDB.setAutoCommit(true);  // Restore auto-commit mode
//            connectDB.close();
//        }
//    }
    public void saveCartAndPay(ActionEvent e) throws IOException, InterruptedException, SQLException {
        if (!validateAction("pay")) {
            System.out.println("Payment action not validated.");
            return;
        }
        String databaseName = "OnlineBookApplication";
        String username = "root";
        String password = "SQLvervipul03@2023";
        String url = "jdbc:mysql://localhost/" + databaseName;
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = null;
        try {

            // Conflicting transaction 1 (For Cart)
            connection.setAutoCommit(false);
            for (String isbn : booksSelected) {
                String checkBookStatus = "SELECT availability FROM BOOK WHERE ISBN = '" + isbn + "'";
                Statement statem = connection.createStatement();
                ResultSet result = statem.executeQuery(checkBookStatus);
                while (result.next()){
                    String s = result.getString(1);
                    if (!(s.equals("In Stock"))){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Sorry the book went out of stock!");
                        alert.setHeaderText(null);
                        alert.setContentText("Remove the book with isbn "+isbn+" from your cart");
                        alert.showAndWait();
                        connection.rollback();
                    }
                }
            }

            String updateCart = "INSERT INTO cart (user_id, date, time, num_items, total_amount) VALUES (?,?,?,?,?)";
            statement = connection.prepareStatement(updateCart);
            statement.setInt(1, currentUser);
            statement.setDate(2, Date.valueOf(LocalDate.now()));
            lt = LocalTime.now();
            statement.setTime(3, Time.valueOf(lt));
            statement.setInt(4, booksSelected.size());
            statement.setInt(5, total_);
            statement.executeUpdate();

            String updateBook = "UPDATE book SET availability = 'Out of Stock', user_id = ?, date = ?, time = ? WHERE ISBN = ?";
            for (String isbn : booksSelected) {
                statement = connection.prepareStatement(updateBook);
                statement.setInt(1, currentUser);
                statement.setDate(2, Date.valueOf(LocalDate.now()));
                statement.setTime(3, Time.valueOf(lt));
                statement.setString(4, isbn);
                statement.executeUpdate();
            }
            connection.commit();
            if (e.getSource().equals(payNow)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bookstoreapplication_final/payment.fxml"));
                Scene scene = new Scene(loader.load(), 600, 400);
                Stage currentStage = new Stage();
                currentStage.initStyle(StageStyle.UNDECORATED);
                currentStage.setScene(scene);
                Stage stage = (Stage) payNow.getScene().getWindow();
                stage.close();
                currentStage.show();
            }
        } catch (SQLException ex) {
            System.out.println("Transaction failed: " + ex.getMessage());
            ex.printStackTrace();
            // Rollback transaction
            try{
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Failed to rollback: " + rollbackEx.getMessage());
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
            connection.setAutoCommit(true);  // Restore auto-commit mode
            connection.close();
        }
    }


    public void b2homeButtonOnAction (ActionEvent e) throws IOException, InterruptedException {
        if (!validateAction("home")) {
            System.out.println("Home action not validated.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bookstoreapplication_final/home.fxml"));
        Scene scene = new Scene(loader.load(), 966, 696);
        Stage currentStage = new Stage();
        currentStage.initStyle(StageStyle.UNDECORATED);
        currentStage.setScene(scene);
        Button clickedButton = (Button) e.getSource();
        Stage stage = (Stage) clickedButton.getScene().getWindow();
        stage.close();
        currentStage.show();

    }

    public boolean validateAction(String actionType) throws IOException {
        String serverAddress = "localhost";
        int port = 543;

        try (Socket socket = new Socket(serverAddress, port);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String request = String.format("action=%s&user=%d", actionType, currentUser);
            writer.write(request);
            writer.newLine();
            writer.flush();
            String response = reader.readLine();

            return "OK".equals(response);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
