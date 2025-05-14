package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class AnalyticsServer {

    public static void main(String[] args) {
        int port = 473;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                    String request = reader.readLine();
                    System.out.println("Received request: " + request);

                    String response = handleRequest(request);
                    writer.write(response);
                    writer.newLine();
                    writer.flush();
                } catch (IOException e) {
                    System.err.println("Error handling client data: " + e.getMessage());
                } finally {
                    socket.close();
                }
            }
        } catch (IOException ex) {
            System.err.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String handleRequest(String request) {
        if (request == null) {
            return "ERROR";
        }

        String[] parts = request.split("&");
        if (parts.length != 2) {
            return "ERROR";
        }

        String actionPart = parts[0];
        String userPart = parts[1];

        if (actionPart.contains("action=saveBook") || actionPart.contains("action=deleteBook")
                || actionPart.contains("action=logout") || actionPart.contains("action=rc")|| actionPart.contains("action=rt")
        || actionPart.contains("action=ro")){
            return "OK";
        }
        return "DENIED";
    }
}
