package org.example.gogame.client;

import org.example.gogame.Board;
import org.example.gogame.StoneColor;
import java.util.Scanner;

public class ConsoleView {
    private Scanner scanner;
    private Board board;

    public ConsoleView(int size) {
        this.scanner = new Scanner(System.in);
        this.board = new Board(size);
    }

    public String getUserInput() {
        printBoard();
        System.out.print("> ");
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return "QUIT";
    }

    public void displayMessage(String msg) {
        printBoard();
        System.out.println("[INFO]: " + msg);
    }

    public void updateBoard(int x, int y, StoneColor color) {
        board.setStone(x, y, color);
    }

    public void clearBoard() {
        board.clear();
    }

    public void printBoard() {
        System.out.print("\033[2J\033[H");
        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                switch (board.getStone(x, y)) {
                    case BLACK -> System.out.print("X");
                    case WHITE -> System.out.print("O");
                    case EMPTY -> System.out.print(".");
                }
            }
            System.out.println();
        }
    }
}