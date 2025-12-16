package org.example.gogame.client;

import java.io.PrintWriter;
import java.net.Socket;

public class GoClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1111);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            ConsoleView view = new ConsoleView(19);

            ServerListener listener = new ServerListener(socket.getInputStream(), view);
            new Thread(listener).start();

            while (true) {
                String command = view.getUserInput();

                if ("quit".equalsIgnoreCase(command)) {
                    out.println("QUIT");
                    break;
                }

                out.println("MOVE " + command);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}