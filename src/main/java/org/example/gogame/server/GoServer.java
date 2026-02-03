package org.example.gogame.server;

import org.example.gogame.StoneColor;
import org.example.gogame.server.db.GameSessionRepository;
import org.example.gogame.server.db.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread;

/**
 * Main entry point for the Go Game Server.
 * Listens for client connections and starts a new Game instance when two players connect.
 *
 * @author Piotr Zieniewicz, Jan Langier
 */
@SpringBootApplication
public class GoServer implements CommandLineRunner {

    private static int port = 1111;
    private PlayerHandler waitingForGame = null;
    private Object lock = new Object();

    @Autowired
    private GameSessionService gService;

    /**
     * Starts the server.
     *
     * @param args Command line arguments (optional port number).
     */
    public static void main(String[] args) {
        SpringApplication.run(GoServer.class, args);
    }

    @Override
    public void run(String... args) {
        start(args);
    }

    /**
     * Runs the server loop, accepting connections and pairing players.
     */
    /**
     * Starts the server.
     *
     * @param args Command line arguments (optional port number).
     */
    public void start(String[] args) {
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Not a port number: " + args[1]);
            }
        } else if (args.length != 0) {
            System.err.println("Malformed arguments. Exiting...");
        }

        System.out.println("Go Server is running on port " + port);

        try (ServerSocket listener = new ServerSocket(port)) {


            while (true) {
                Socket socket = listener.accept();
                new Thread(() -> {
                    try {
                        InputStream is = socket.getInputStream();

                        int b = is.read();
                        switch (b) {
                            case 1: // review
                                System.out.println("review");
                                Review review = new Review(socket, gService);
                                new Thread(review).start();
                            break;

                            case 2: // bot
                                PlayerHandler player1 = new PlayerHandler(socket, StoneColor.BLACK);
                                BotPlayerHandler bot = new BotPlayerHandler(StoneColor.WHITE);
                                Game botgame = new Game(player1, bot, 19, gService);

                                break;

                            default: // game
                                synchronized(lock) {
                                    if (waitingForGame != null) {
                                        PlayerHandler player2 = new PlayerHandler(socket, StoneColor.WHITE);
                                        player2.sendMessage("MESSAGE Connected as WHITE. Game starting...");

                                        System.out.println("Both players connected. Starting game.");
                                        Game game = new Game(waitingForGame, player2, 19, gService);
                                        waitingForGame = null;
                                    } else {
                                        waitingForGame = new PlayerHandler(socket, StoneColor.BLACK);
                                        waitingForGame.sendMessage("MESSAGE Connected as BLACK. Waiting for opponent...");
                                        System.out.println("Player 1 connected. Waiting for Player 2 (WHITE)...");
                                    }
                                }
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
