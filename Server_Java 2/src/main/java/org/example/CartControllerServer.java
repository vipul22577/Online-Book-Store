package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CartControllerServer {

    public static void main(String[] args) {
        int port = 543;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("New client connected");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    String request = reader.readLine();
                    System.out.println("Received: " + request);

                    if (isValidRequest(request)) {
                        writer.write("OK");
                    } else {
                        writer.write("DENIED");
                    }
                    writer.newLine();
                    writer.flush();
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isValidRequest(String request) {
        if (request == null) {
            return false;
        }
        // Simple validation logic (could be expanded based on actual validation needs)
        return request.contains("action=refresh") || request.contains("action=remove")
                || request.contains("action=pay")||request.contains("action=home") ;
    }
}
