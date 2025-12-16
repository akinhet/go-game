package org.example.gogame.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerListener implements Runnable {
    private BufferedReader in;
    private ClientGameController controller;

    public ServerListener(InputStream inputStream, ClientGameController controller) {
        this.in = new BufferedReader(new InputStreamReader(inputStream));
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            String response;
            while ((response = in.readLine()) != null) {
                controller.handleServerMessage(response);
            }
        } catch (IOException e) {
            System.out.println("Connection closed.");
            controller.handleConnectionError();
        }
    }
}