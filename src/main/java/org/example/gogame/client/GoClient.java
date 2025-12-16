package org.example.gogame.client;

import java.net.Socket;

public class GoClient {
    public static void main(String[] args) {
        try {
            System.out.println("Connecting to server...");
            Socket socket = new Socket("localhost", 1111);

            ConsoleView view = new ConsoleView(19);
            ClientGameController controller = new ClientGameController(socket, view);

            controller.play();

        } catch (Exception e) {
            System.err.println("Client Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}