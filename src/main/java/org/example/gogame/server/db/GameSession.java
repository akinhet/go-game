package org.example.gogame.server.db;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.example.gogame.StoneColor;

@Entity
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime datePlayed;
    private StoneColor winner;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Move> moves = new ArrayList<>();

    public GameSession() {
        this.datePlayed = LocalDateTime.now();
    }

    public void addMove(Move move) {
        moves.add(move);
        move.setGameSession(this);
    }

    public List<Move> getMoves() { return moves; }
    public void setWinner(StoneColor w) { this.winner = w; }
    public LocalDateTime getDatePlayed() { return datePlayed; }
    public StoneColor getWinner() { return winner; }
    public Long getId() { return id; }
}
