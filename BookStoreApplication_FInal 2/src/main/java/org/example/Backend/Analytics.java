package org.example.Backend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.Frontend.BookStoreApplication_Final;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.time.LocalDate;

import static org.example.Frontend.BookStoreController.currentUser;


public class Analytics {
    @FXML
    private Button logout;
    @FXML
    private Button saveBook;
    @FXML
    private Button deleteBook;
    @FXML
    private Button updateBook;
    @FXML
    private TextField addISBN;
    @FXML
    private TextField addTitle;
    @FXML
    private TextField addEd;
    @FXML
    private TextField addLang;
    @FXML
    private TextField addType;
    @FXML
    private TextField delISBN;
    @FXML
    private TextField upISBN;
    @FXML
    private TextField upEd;
    @FXML
    private CheckBox today;
    @FXML
    private CheckBox month;
    @FXML
    private CheckBox allTime;
    @FXML
    private Button rt;
    @FXML
    private Button rc;
    @FXML
    private Button ro;
    @FXML
    private TextField custStatus;
    @FXML
    private TextField custCountry;
    @FXML
    private TextField ageLow;
    @FXML
    private TextField ageHigh;
    @FXML
    private TextField gender;
    @FXML
    private Label trending;
    @FXML
    private Label custInfo;
    @FXML
    private Label orders;
    @FXML
    private TextField ID;
    public DatabaseConnection connectNow = new DatabaseConnection();
    public Connection connectDB = connectNow.getConnection();
    public void logoutButtonOnAction(ActionEvent e) throws IOException, SQLException {
        if (validateAction("logout"))  {
            connectDB.close();
            Stage stage_ = (Stage) logout.getScene().getWindow();
            stage_.close();
            FXMLLoader fxmlLoader = new FXMLLoader(BookStoreApplication_Final.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            Button clickedButton = (Button) e.getSource();
            Stage prevstage = (Stage) clickedButton.getScene().getWindow();
            prevstage.close();
            stage.show();
        }
        else {
            System.out.println("Action Denied by Server");

        }
    }
//    public void saveBookButtonOnAction(ActionEvent e) throws SQLException, IOException {
//        if (validateAction("saveBook"))  {
//            connectDB.setAutoCommit(false);
//            int ISBN = Integer.parseInt(addISBN.getText());
//            String title = addTitle.getText();
//            int ed = Integer.parseInt(addEd.getText());
//            String lang = addLang.getText();
//            String type = addType.getText();
//            String addBook = "INSERT INTO book (ISBN, type, status, availability, edition, language, title, rating) VALUES (?,?,?,?,?,?,?,?)";
//            PreparedStatement statement = connectDB.prepareStatement(addBook);
//            statement.setInt(1, ISBN);
//            statement.setString(2, type);
//            statement.setString(3, "Available");
//            statement.setString(4, "In Stock");
//            statement.setInt(5, ed);
//            statement.setString(6, lang);
//            statement.setString(7, title);
//            statement.setString(8, "4.0");
//            int rowsAffected = statement.executeUpdate();
//            if (rowsAffected == 1)
//                System.out.println("Book added successfully");
//        }
//        else{
//            System.out.println("Action Denied by Server");
//        }
//    }
public void saveBookButtonOnAction(ActionEvent e) throws SQLException, IOException {
    if (validateAction("saveBook")) {
        connectDB.setAutoCommit(false); // Ensure auto-commit is off to manage transaction manually
        int ISBN = Integer.parseInt(addISBN.getText());
        String title = addTitle.getText();
        int ed = Integer.parseInt(addEd.getText());
        String lang = addLang.getText();
        String type = addType.getText();
        String addBook = "INSERT INTO book (ISBN, type, status, availability, edition, language, title, rating) VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement statement = connectDB.prepareStatement(addBook)) {
            statement.setInt(1, ISBN);
            statement.setString(2, type);
            statement.setString(3, "Available");
            statement.setString(4, "In Stock");
            statement.setInt(5, ed);
            statement.setString(6, lang);
            statement.setString(7, title);
            statement.setString(8, "4.0");
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 1) {
                connectDB.commit(); // Commit the transaction explicitly
                System.out.println("Book added successfully");
            } else {
                System.out.println("No rows affected.");
            }
        }
    } else {
        System.out.println("Action Denied by Server");
    }
}

    public void deleteBookButtonOnAction(ActionEvent e) throws SQLException, IOException {
        if (validateAction("deleteBook")) {
            connectDB.setAutoCommit(false);
            int ISBN = Integer.parseInt(delISBN.getText());
            String deleteInventorySql = "DELETE FROM BookInventory WHERE book_id = ?";
            try (PreparedStatement deleteInventoryStmt = connectDB.prepareStatement(deleteInventorySql)) {
                deleteInventoryStmt.setString(1, "9876543210");
                deleteInventoryStmt.executeUpdate();
            }
            connectDB.commit();
            System.out.println("Transaction 4 completed successfully.");
        }
    }
//
//            String delete = "DELETE FROM book WHERE ISBN = ?";
//            PreparedStatement statement = connectDB.prepareStatement(delete);
//            statement.setInt(1, ISBN);
//            int rowsAffected = statement.executeUpdate();
//            if (rowsAffected == 1)
//                System.out.println("Book deleted successfully");
//        }
//        else{
//            System.out.println("Action Denied by Server");
//        }

    public void updateBookButtonOnAction(ActionEvent e) throws SQLException, IOException {
        if (validateAction("update")) {
            connectDB.setAutoCommit(false);
            String updatePriceSql = "UPDATE BookInventory SET price = ? WHERE book_id = ?";
            try (PreparedStatement updatePriceStmt = connectDB.prepareStatement(updatePriceSql)) {
                updatePriceStmt.setDouble(1, 25.00);
                updatePriceStmt.setString(2, "1234567890");
                updatePriceStmt.executeUpdate();
            }
            connectDB.commit();
            System.out.println("Transaction 3 completed successfully.");
        }



//            int ISBN = Integer.parseInt(upISBN.getText());
//            int ed = Integer.parseInt(upEd.getText());
//            String updateEd = "UPDATE book SET edition = ? WHERE ISBN = ?";
//            PreparedStatement statement = connectDB.prepareStatement(updateEd);
//            statement.setInt(1, ed);
//            statement.setInt(2, ISBN);
//            int rowsAffected = statement.executeUpdate();
//            if (rowsAffected == 1)
//                System.out.println("Book deleted successfully");
//        }
//        else{
//            System.out.println("Action Denied by Server");
//        }

    }
    public void rtButtonOnAction(ActionEvent e) throws SQLException, IOException {
        if (validateAction("rt")) {
            LocalDate ld = LocalDate.now();
            String trends = "";
            if (today.isSelected()) {
                trends = "SELECT ISBN, type, status, availability, edition, language, title, rating FROM book WHERE " +
                        "date = '" + ld + "' ORDER BY time DESC";
            }
            if (month.isSelected()) {
                trends = "SELECT ISBN, type, status, availability, edition, language, title, rating FROM book WHERE " +
                        "MONTH(date) = '" + ld.getMonthValue() + "' ORDER BY date, time DESC";
            }
            if (allTime.isSelected()) {
                trends = "SELECT ISBN, type, status, availability, edition, language, title, rating FROM book ORDER BY date, time DESC";
            }
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(trends);
            String ans = "";
            while (resultSet.next()) {
                ans += resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " "
                        + resultSet.getString(4) + " " + resultSet.getString(5) + " " + resultSet.getString(6)
                        + " " + resultSet.getString(7) + " " + resultSet.getString(8) + "\n";
            }
            trending.setText(ans);
        }
        else{
            System.out.println("Action Denied by Server");
        }
    }
    public void rcButtonOnAction(ActionEvent e) throws SQLException, IOException {
        if (validateAction("rc"))  {
            String status = custStatus.getText();
            String country = custCountry.getText();
            int low = Integer.parseInt(ageLow.getText());
            int high = Integer.parseInt(ageHigh.getText());
            String genderText = gender.getText();
            String customer = "SELECT user_id, username, password, first_name, last_name, house_num, street, city, country, age, gender, login_status," +
                    " wallet, customer_status FROM customer WHERE customer_status = '" + status + "' AND country = '" + country + "'" +
                    "AND age >= '" + low + "' AND age <= '" + high + "' AND gender = '" + genderText + "'";
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(customer);
            String ans = "";
            while (resultSet.next()) {
                ans += "%d %s %s %s %s %d %s %s %s %d %s %s %d %s".formatted(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6), resultSet.getString(7),
                        resultSet.getString(8), resultSet.getString(9), resultSet.getInt(10), resultSet.getString(11),
                        resultSet.getString(12), resultSet.getInt(13), resultSet.getString(14));
                ans += "\n";
            }
            custInfo.setText(ans);
        }
        else{
            System.out.println("Action Denied by Server");
        }

    }
    public void roButtonOnAction(ActionEvent e) throws SQLException, IOException {
        if (validateAction("ro"))  {
            int id = Integer.parseInt(ID.getText());
            String order = "SELECT history.transaction_id, history.user_id, history.date, history.time, cart.num_items, cart.total_amount, " +
                    "book.ISBN, book.title FROM history INNER JOIN cart ON history.user_id = cart.user_id AND history.date = cart.date AND history.time = cart.time  INNER JOIN book ON " +
                    "history.user_id = book.user_id AND history.time = book.time WHERE history.user_id = '" + id + "'";
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(order);
            String ans = "";
            while (resultSet.next()) {
                ans += "%d %d %s %s %d %d %d %s".formatted(resultSet.getInt(1), resultSet.getInt(2),
                        resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5)
                        , resultSet.getInt(6), resultSet.getInt(7), resultSet.getString(8));
                ans += "\n";
            }
            orders.setText(ans);
        }
        else{
            System.out.println("Action Denied by Server");
        }

    }
    private boolean validateAction(String actionType) throws IOException {
        // Server details
        String serverAddress = "localhost";  // Change to your server's IP address if not running locally
        int port = 473;

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
