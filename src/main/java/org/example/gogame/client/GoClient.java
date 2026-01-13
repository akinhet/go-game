package org.example.gogame.client;

import java.net.Socket;

/**
 * Main entry point for the Go Game Client application.
 * Establishes connection to server and initializes the controller and view.
 *
 * @author toBeSpecified
 */
public class GoClient {
    /**
     * Main method to start the client.
     *
     * @param args Command line arguments.
     */
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