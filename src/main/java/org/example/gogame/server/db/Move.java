package org.example.gogame.server.db;

import org.example.gogame.StoneColor;

import jakarta.persistence.*;

@Entity
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int moveNumber;
    private String moveString;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameSession game;

    public Move() {}

    public Move(int moveNumber, String moveString) {
        this.moveNumber = moveNumber;
        this.moveString = moveString;
    }

    public void setGameSession(GameSession game) { this.game = game; }
    public int getMoveNumber() { return moveNumber; }
    public String getMoveString() { return moveString; }
}
