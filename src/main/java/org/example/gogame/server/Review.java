package org.example.gogame.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;

import org.example.gogame.server.db.GameSessionService;
import org.example.gogame.server.db.GameSession;
import org.example.gogame.server.db.Move;

public class Review implements Runnable {
    private final Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private final GameSessionService gService;

    public Review(Socket socket, GameSessionService gService) {
        this.socket = socket;
        this.gService = gService;
    }

    @Override
    public void run() {
        try {
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output.println("START_LIST");

            List<GameSession> games = gService.getAllGames(); 

            for (GameSession g : games) {
                output.println(g.getId() + " " + g.getDatePlayed() + " " + g.getWinner());
            }
            output.println("END_LIST");

            String gameIdStr = input.readLine();
            if (gameIdStr == null)
                return;

            try {
                Long gameId = Long.parseLong(gameIdStr);

                List<Move> moves = gService.getMovesForReplay(gameId);
                moves.sort(Comparator.comparingInt(Move::getMoveNumber));

                int currentMoveIndex = 1;
                String command;

                while ((command = input.readLine()) != null) {
                    if (currentMoveIndex < moves.size()) {
                        Move move = moves.get(currentMoveIndex);

                        output.println(move.getMoveString());

                        currentMoveIndex++;
                    } else {
                        output.println("END_GAME");
                        break;
                    }
                }

            } catch (NumberFormatException e) {
                System.err.println("Invalid game ID format received: " + gameIdStr);
                output.println("ERROR Invalid ID");
            } catch (Exception e) {
                System.err.println("Error fetching game: " + e.getMessage());
                output.println("ERROR Game not found");
            }

        } catch (IOException e) {
            System.err.println("Client disconnected during review: " + e.getMessage());
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
