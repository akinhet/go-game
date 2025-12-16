package org.example.gogame.client;

import org.example.gogame.StoneColor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerListener implements Runnable {
    private BufferedReader in;
    private ConsoleView view;

    public ServerListener(InputStream inputStream, ConsoleView view) {
        this.in = new BufferedReader(new InputStreamReader(inputStream));
        this.view = view;
    }

    @Override
    public void run() {
        try {
            String response;
            while ((response = in.readLine()) != null) {
                if (response.startsWith("MESSAGE")) {
                    String msg = response.substring(8);
                    view.displayMessage(msg);
                }
                else if (response.startsWith("MOVE")) {
                    String[] parts = response.split(" ");
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    String color = parts[3];

                    view.updateBoard(x, y, StoneColor.valueOf(color));
                }
                else if (response.startsWith("CAPTURES")) {
                    String[] parts = response.split(" ");
                    for (int i = 1; i + 1 < parts.length; i++) {
                        int x, y;
                        try {
                            x = Integer.parseInt(parts[i]);
                            y = Integer.parseInt(parts[i+1]);
                        } catch (NumberFormatException e) {
                            System.err.println("ERROR: Malformed packet");
                            continue;
                        }
                        view.updateBoard(x, y, StoneColor.EMPTY);
                        view.printBoard();
                    }
                }
                else if (response.startsWith("GAME_OVER")) {
                }
            }
        } catch (Exception e) {
            System.out.println("Server connection lost.");
        }
    }
}