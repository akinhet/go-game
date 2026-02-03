package org.example.gogame.server;

import org.example.gogame.Board;
import org.example.gogame.StoneColor;
import java.util.ArrayList;
import java.util.Random;

public class BotPlayerHandler extends PlayerHandler {

    private final Board internalBoard;
    private final GameLogic gameLogic;
    private final Random random;

    private ArrayList<int[]> legalMoves = new ArrayList<>();
    private ArrayList<int[]> attackMoves = new ArrayList<>();

    public BotPlayerHandler(StoneColor color) {
        super(null, color);
        this.gameLogic = new GameLogic();
        this.random = new Random();
        this.internalBoard = new Board(19);
    }

    @Override
    public void run() {
        System.out.println("Bot " + getColor() + " uruchomiony i gotowy do gry.");
    }

    @Override
    public void sendMessage(String message) {

        if (message.startsWith("MOVE")) {
            String[] parts = message.split(" ");
            if (parts.length >= 4) {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                StoneColor color = StoneColor.valueOf(parts[3]);
                internalBoard.setStone(x, y, color);
            }
        }
        else if (message.startsWith("TURN")) {
            String turnColor = message.split(" ")[1];
            if (turnColor.equals(getColor().name())) {
                makeBotMove();
            }
        }
        else if (message.startsWith("CAPTURES")) {
            String[] parts = message.split(" ");
            for (int i = 1; i < parts.length - 1; i += 2) {
                int x = Integer.parseInt(parts[i]);
                int y = Integer.parseInt(parts[i+1]);
                internalBoard.setStone(x, y, StoneColor.EMPTY);
            }
        }
        else if (message.startsWith("ERROR")){
            if (!message.equals("ERROR Game stopped.")){
                remakeBotMove();
            }
        }
        else if (message.startsWith("NEGOTIATION")){
            getGame().processAgree(this);
        }
    }

    synchronized private void makeBotMove() {
        try { Thread.sleep(500); } catch (InterruptedException e) {}


        StoneColor opponentColor = (getColor() == StoneColor.BLACK) ? StoneColor.WHITE : StoneColor.BLACK;
        int size = internalBoard.getSize();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (gameLogic.validateMove(internalBoard, x, y)) {
                    int[] move = {x, y};
                    legalMoves.add(move);

                    if (isNeighboringOpponent(x, y, opponentColor)) {
                        attackMoves.add(move);
                    }
                }
            }
        }

        int[] chosenMove;
        if (!attackMoves.isEmpty()) {
            chosenMove = attackMoves.get(random.nextInt(attackMoves.size()));
            attackMoves.remove(chosenMove);
            legalMoves.remove(chosenMove);
        } else if (!legalMoves.isEmpty()) {
            chosenMove = legalMoves.get(random.nextInt(legalMoves.size()));
            legalMoves.remove(chosenMove);
        } else {
            if (getGame() != null) getGame().processPass(this);
            return;
        }
        int x = chosenMove[0];
        int y = chosenMove[1];
        attackMoves.removeIf(m -> m[0] == x && m[1] == y);
        legalMoves.removeIf(m -> m[0] == x && m[1] == y);

        if (getGame() != null) {
            System.out.println("Move White "+ chosenMove[0]+" "+chosenMove[1]);
            getGame().processMove(chosenMove[0], chosenMove[1], this);
        }
    }
    synchronized private void remakeBotMove(){
        int[] chosenMove;
        if (!attackMoves.isEmpty()) {
            chosenMove = attackMoves.get(random.nextInt(attackMoves.size()));
        } else if (!legalMoves.isEmpty()) {
            chosenMove = legalMoves.get(random.nextInt(legalMoves.size()));
        } else {
            if (getGame() != null) getGame().processPass(this);
            return;
        }
        int x = chosenMove[0];
        int y = chosenMove[1];
        attackMoves.removeIf(m -> m[0] == x && m[1] == y);
        legalMoves.removeIf(m -> m[0] == x && m[1] == y);


        if (getGame() != null) {
            System.out.println("Move White "+ chosenMove[0]+" "+chosenMove[1]);
            getGame().processMove(chosenMove[0], chosenMove[1], this);
        }
    }

    private boolean isNeighboringOpponent(int x, int y, StoneColor opponent) {
        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for (int[] d : dirs) {
            int nx = x + d[0];
            int ny = y + d[1];
            if (nx >= 0 && nx < internalBoard.getSize() && ny >= 0 && ny < internalBoard.getSize()) {
                if (internalBoard.getStone(nx, ny) == opponent) return true;
            }
        }
        return false;
    }
}