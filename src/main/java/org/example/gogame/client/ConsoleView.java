package org.example.gogame.client;

import org.example.gogame.Board;
import org.example.gogame.StoneColor;
import java.util.Scanner;

public class ConsoleView {
    private Scanner scanner;
    private Board board;
    private String color;
    private String msg = "";
    private String err = "";
    private boolean myTurn = false;

    public ConsoleView(int size) {
        this.board = new Board(size);
        this.scanner = new Scanner(System.in);

        board.clear();
    }

    public void updateBoard(int x, int y, StoneColor color) {
        board.setStone(x, y, color);
    }

    public void displayBoard() {
        System.out.print("\033[2J\033[H");
        System.out.println(">> " + color + " <<");
        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                if (board.getStone(x, y) == StoneColor.BLACK)
                    System.out.print("X");
                else if (board.getStone(x, y) == StoneColor.WHITE)
                    System.out.print("O");
                else
                    System.out.print(".");
            }
            System.out.println();
        }

        System.out.println(">> " + msg + " <<");
        if (!err.isEmpty()) {
            System.out.println(err);
            err = "";
        }
        if (myTurn)
            System.out.print("> ");
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public String getUserInput() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return "quit";
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTurn(boolean turn) {
        myTurn = turn;
    }

    public void setErr(String err) {
        this.err = err;
    }
}