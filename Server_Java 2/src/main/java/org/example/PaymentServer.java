package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PaymentServer {

    private static final int PORT = 879;

    public static void main(String[] args) {
        System.out.println("Server is starting...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                    String requestLine = reader.readLine();
                    System.out.println("Received from client: " + requestLine);

                    Map<String, String> requestData = parseRequest(requestLine);

                    if (requestData.containsKey("action")) {
                        String response = handleAction(requestData);
                        System.out.println(response);
                        writer.write(response);
                        writer.newLine();
                        writer.flush();
                    }

                } catch (IOException e) {
                    System.err.println("Error handling client connection: " + e.getMessage());
                } finally {
                    System.out.println("Client disconnected");
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Server failed to start: " + e.getMessage());
        }
    }

    private static Map<String, String> parseRequest(String request) {
        Map<String, String> map = new HashMap<>();
        String[] params = request.split("&");
        for (String param : params) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                map.put(entry[0], entry[1]);
            }
        }
        return map;
    }

    private static String handleAction(Map<String, String> requestData) {
        String action = requestData.get("action");
        switch (action) {
            case "refresh":
            case "pay":
            case "cancel":
                return "OK";
            default:
                return "ERROR";
        }
    }
}
