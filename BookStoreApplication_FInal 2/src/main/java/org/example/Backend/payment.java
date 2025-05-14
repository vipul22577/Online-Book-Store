package org.example.Backend;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static org.example.Backend.CartController.lt;
import static org.example.Backend.CartController.total_;
import static org.example.Backend.HomeController.booksSelected;
import static org.example.Frontend.BookStoreController.currentUser;

public class payment {
    @FXML
    private Label user_id;
    @FXML
    private Label numBooks;
    @FXML
    private Label total;
    @FXML
    private Label address;
    @FXML
    private Label dateTime;
    @FXML
    private Button refresh;
    @FXML
    private Button backToHome;
    @FXML
    private Button pay;
    public static int paymentDone = 0;
    public DatabaseConnection connectNow = new DatabaseConnection();
    public Connection connection = connectNow.getConnection();
    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            dateTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd   HH:mm:ss")));
        }
    };
    public void refreshButtonOnAction(ActionEvent e) throws SQLException, IOException {
        if (validateAction("refresh")) {
            timer.start();
            user_id.setText(String.valueOf(currentUser));
            numBooks.setText(String.valueOf(booksSelected.size()));
            total.setText(String.valueOf(total_));

            String findAddress = "SELECT house_num, street, city, country FROM customer WHERE user_id = '"+ currentUser+"'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(findAddress);
            String addr = "";
            while (resultSet.next()){
                addr += resultSet.getInt(1) +"\n"+ resultSet.getString(2)
                        +"\n"+ resultSet.getString(3)+"\n"+ resultSet.getString(4);
            }
            address.setText(addr);
        } else {
            System.out.println("Action Denied by Server");
        }
    }
    public void cancelTransaction(ActionEvent e) throws IOException {
        if (validateAction("cancel")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bookstoreapplication_final/cart.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage currentStage = new Stage();
            currentStage.initStyle(StageStyle.UNDECORATED);
            currentStage.setScene(scene);
            Button clickedButton = (Button) e.getSource();
            Stage stage = (Stage) clickedButton.getScene().getWindow();
            stage.close();
            currentStage.show();
        }
        else {
            System.out.println("Action Denied by Server");

        }
    }
//    public void payFinal(ActionEvent e) throws SQLException, IOException {
//        String databaseName = "OnlineBookApplication";
//        String username = "root";
//        String password = "SQLvervipul03@2023";
//        String url = "jdbc:mysql://localhost/" + databaseName;
//        Connection connection = DriverManager.getConnection(url, username, password);
//        PreparedStatement statement = null;
//
//        try {
//            connection.setAutoCommit(false);
//            String transaction = "INSERT INTO history (user_id, date, time) VALUES (?,?,?)";
//            statement = connection.prepareStatement(transaction);
//            statement.setInt(1, currentUser);
//            statement.setDate(2, Date.valueOf(LocalDate.now()));
//            statement.setTime(3, Time.valueOf(lt));
//            int rowsAffected = statement.executeUpdate();
//            System.out.println("Rows affected: " + rowsAffected);
//            connection.commit();
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Transaction Successful");
//            alert.setHeaderText(null);
//            alert.setContentText("Thank you for your purchase. You will be redirected to the home page.");
//            alert.showAndWait();
//            paymentDone = 1;
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bookstoreapplication_final/home.fxml"));
//            Scene scene = new Scene(loader.load(), 966, 696);
//            Stage currentStage = new Stage();
//            currentStage.initStyle(StageStyle.UNDECORATED);
//            currentStage.setScene(scene);
//            Button clickedButton = (Button) e.getSource();
//            Stage stage = (Stage) clickedButton.getScene().getWindow();
//            stage.close();
//            currentStage.show();
//        } catch (SQLException ex) {
//            if (connection != null) {
//                try {
//                    connection.rollback(); // Rollback transaction if any exception occurs
//                    System.out.println("Transaction rolled back due to errors");
//                } catch (SQLException exRollback) {
//                    System.err.println("Rollback failed: " + exRollback.getMessage());
//                }
//            }
//            System.out.println("Transaction failed: " + ex.getMessage());
//            ex.printStackTrace();
//            throw ex;
//        } finally {
//            if (statement != null) {
//                statement.close();
//            }
//            if (connection != null) {
//                connection.setAutoCommit(true); // Reset auto-commit to true
//                connection.close();
//            }
//        }
//    }

//    public void payFinal(ActionEvent e) throws SQLException, IOException {
//        if (validateAction("pay")) {
//            String transaction = "INSERT INTO history (user_id, date, time) VALUES (?,?,?)";
//            PreparedStatement statement = connection.prepareStatement(transaction);
//            statement.setInt(1, currentUser);
//            statement.setDate(2, Date.valueOf(LocalDate.now()));
//            statement.setTime(3, Time.valueOf(lt));
//            int rowsAffected = statement.executeUpdate();
//            System.out.println(rowsAffected);
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("THANK YOU");
//            alert.setHeaderText(null);
//            alert.setContentText("Items purchased you'll be redirected to Home!");
//            paymentDone = 1;
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bookstoreapplication_final/home.fxml"));
//            Scene scene = new Scene(loader.load(), 966, 696);
//            Stage currentStage = new Stage();
//            currentStage.initStyle(StageStyle.UNDECORATED);
//            currentStage.setScene(scene);
//            Button clickedButton = (Button) e.getSource();
//            Stage stage = (Stage) clickedButton.getScene().getWindow();
//            stage.close();
//            currentStage.show();
//        }
//        else {
//            System.out.println("Action Denied by Server");
//
//        }
    public void payFinal(ActionEvent e) throws SQLException, IOException {
        if (validateAction("pay")) {
            PreparedStatement statement = null;
            Connection connectDB = connectNow.getConnection();
            try {
                connectDB.setAutoCommit(false);
                String checkBookStatus = "SELECT wallet FROM Customer WHERE id = '" + currentUser + "'";
                Statement statem = connectDB.createStatement();
                ResultSet result = statem.executeQuery(checkBookStatus);
                while (result.next()){
                    int money = result.getInt(1);
                    if (money < 500) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Transaction Failed");
                        alert.setHeaderText(null);
                        alert.setContentText("Add at least 500 rupees to your wallet.");
                        alert.showAndWait();

                        connectDB.rollback();
                    }
                }

                Scanner sc = new Scanner(System.in);
                System.out.println("Add cash to wallet: ");
                int money = sc.nextInt();
                String walletUpdate = "UPDATE customer SET wallet = ? WHERE id = ?";
                statement = connectDB.prepareStatement(walletUpdate);
                statement.setInt(1, money);
                statement.setInt(2, currentUser);
                int rowsAffected = statement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);

                String transaction = "INSERT INTO history (user_id, date, time) VALUES (?,?,?)";
                statement = connectDB.prepareStatement(transaction);
                statement.setInt(1, currentUser);
                statement.setDate(2, Date.valueOf(LocalDate.now()));
                statement.setTime(3, Time.valueOf(lt));
                rowsAffected = statement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
                connectDB.commit();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Transaction Successful");
                alert.setHeaderText(null);
                alert.setContentText("Thank you for your purchase. You will be redirected to the home page.");
                alert.showAndWait();
                paymentDone = 1;

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bookstoreapplication_final/home.fxml"));
                Scene scene = new Scene(loader.load(), 966, 696);
                Stage currentStage = new Stage();
                currentStage.initStyle(StageStyle.UNDECORATED);
                currentStage.setScene(scene);
                Button clickedButton = (Button) e.getSource();
                Stage stage = (Stage) clickedButton.getScene().getWindow();
                stage.close();
                currentStage.show();
            } catch (SQLException ex) {
                if (connectDB != null) {
                    try {
                        connectDB.rollback(); // Rollback transaction if any exception occurs
                        System.out.println("Transaction rolled back due to errors");
                    } catch (SQLException exRollback) {
                        System.err.println("Rollback failed: " + exRollback.getMessage());
                    }
                }
                System.out.println("Transaction failed: " + ex.getMessage());
                ex.printStackTrace();
                throw ex;
            } finally {
                if (statement != null) {
                    statement.close();
                }
                if (connectDB != null) {
                    connectDB.setAutoCommit(true); // Reset auto-commit to true
                    connectDB.close();
                }
            }
        }
    }

    private boolean validateAction(String actionType) throws IOException {
        // Server details
        String serverAddress = "localhost";  // Change to your server's IP address if not running locally
        int port = 879;  // Change to your server's listening port

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
    }
}
