package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
public class LoginServer {
    public static void main(String[] args) {
        int port = 175;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("New client connected");

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                        String line = reader.readLine();
                        ObjectMapper mapper = new ObjectMapper();
                        Map<String, String> requestMap = mapper.readValue(line, Map.class);
                        String username = requestMap.get("username");
                        String password = requestMap.get("password");

                        Map<String, Object> responseMap = authenticateUser(username, password);

                        String response = mapper.writeValueAsString(responseMap);
                        writer.write(response);
                        writer.newLine();
                        writer.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Map<String, Object> authenticateUser(String username, String password) {
        Map<String, Object> responseMap = new HashMap<>();
        String sql = "SELECT user_id FROM customer WHERE username = ? AND password = ?";
        System.out.println(sql);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                responseMap.put("status", "success");
                responseMap.put("userId", resultSet.getInt("user_id"));
            } else {
                responseMap.put("status", "failure");
            }
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
            responseMap.put("status", "error");
            responseMap.put("message", "Internal server error");
        }

        return responseMap;
    }
}
