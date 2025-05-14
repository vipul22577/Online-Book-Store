package org.example;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HomeControllerServer {
    public static void main(String[] args) {
        int port = 243;  // Different port from LoginServer
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Action Server is listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                    System.out.println("New action request received");
                    String line = reader.readLine();
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, String> requestMap = mapper.readValue(line, Map.class);
                    String action = requestMap.get("action");
                    System.out.println(action);
                    Map<String, Object> responseMap = new HashMap<>();
                    String isbn = requestMap.get("isbn");
                    System.out.println(isbn);
                    if("logout".equals(isbn)){
                        responseMap.put("status", "success");

                    }
                    else if("cart-view".equals(isbn)){
                        responseMap.put("status", "success");

                    }
                    else if("refresh-stock".equals(isbn)){
                        responseMap.put("status", "success");

                    }
                    else if("Review".equals(isbn)){
                        responseMap.put("status", "success");

                    }

                    else {
                        responseMap.put("status", "success");
                    }

                    String response = mapper.writeValueAsString(responseMap);
                    writer.write(response);
                    writer.newLine();
                    writer.flush();
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
