package org.example.Frontend;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class BookStoreController {
    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField userType;
    public static int currentUser = 0;
    static int attemptCount = 0;

    public void loginButtonOnAction(ActionEvent e) {
        if (userType.getText().equals("Customer")) {
            attemptCount++;
            if (attemptCount < 3) {
                if (!usernameTextField.getText().isBlank() && !passwordPasswordField.getText().isBlank()) {
                    validateLogin();
                } else {
                    loginMessageLabel.setText("Please enter your\nusername and password");
                }
            } else {
                loginMessageLabel.setText("Login Blocked.\nPress the CANCEL button.");
            }
        } else {
            // Handle admin login here if necessary
        }
    }

    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin() {
        String serverAddress = "localhost"; // Change this to your server's IP or hostname
        int serverPort = 175; // Change this to your server's port for login handling
        ObjectMapper objectMapper = new ObjectMapper();

        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(Map.of(
                    "username", usernameTextField.getText(),
                    "password", passwordPasswordField.getText()
            ));
        } catch (Exception ex) {
            ex.printStackTrace();
            Platform.runLater(() -> loginMessageLabel.setText("Error creating login request."));
            return;
        }

        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            writer.write(requestBody);
            writer.newLine();
            writer.flush();
            String response = reader.readLine();
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            Platform.runLater(() -> {
                System.out.println(responseMap.get("status"));
                if ("success".equals(responseMap.get("status"))) {
                    currentUser = (Integer) responseMap.get("userId");
                    attemptCount = 0;
                    loginMessageLabel.setText("Welcome!");
                    showHomeScene();
                } else {
                    loginMessageLabel.setText("Login failed.");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> loginMessageLabel.setText("Connection error."));
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> loginMessageLabel.setText("Error processing login response."));
        }
    }


    private void showHomeScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bookstoreapplication_final/home.fxml"));
        try {
            Scene scene = new Scene(loader.load(), 966, 696);
            Stage currentStage = new Stage();
            currentStage.initStyle(StageStyle.UNDECORATED);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
