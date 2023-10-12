package chat_ant;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static final int SERVER_PORT = 3000;
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        System.out.println("Server started on port " + SERVER_PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String[] parts = reader.readLine().split(" ");
            String username = parts[0].substring(0, parts[0].length() - 1);
            System.out.println("Username of connected client: " + username);
            
            ClientHandler clientHandler = new ClientHandler(clientSocket, username);
            clients.add(clientHandler);
            clientHandler.start();
        }
    }

    static class ClientHandler extends Thread {

        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String username;

        public ClientHandler(Socket socket, String username) {
            this.socket = socket;
            this.username = username;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Received: " + message);
                    broadcast(message, username);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                    writer.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(this);
            }
        }

        public void broadcast(String message, String recipient) {
            for (ClientHandler client : clients) {
                if (!client.username.equals(username)) {
                    if (client.username.equals(recipient) || username.equals(recipient)) {
                        client.writer.println(message);
                    }
                }
            }
        }
    }
}
