package org.example.gogame.server.db;

import org.example.gogame.StoneColor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
public class GameSessionService {
    private final GameSessionRepository gameRepository;

    public GameSessionService(GameSessionRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Zapisuje zakończoną grę wraz z całą listą ruchów.
     */
    @Transactional
    public void saveCompletedGameSession(StoneColor winner, List<Move> moveHistory) {
        GameSession game = new GameSession();
        game.setWinner(winner);

        for (Move move : moveHistory) {
            game.addMove(move);
        }

        gameRepository.save(game);
    }

    /**
     * Pobiera grę i jej ruchy posortowane chronologicznie.
     */
    @Transactional
    public List<Move> getMovesForReplay(Long gameId) {
        GameSession game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("GameSession not found"));

        List<Move> sortedMoves = game.getMoves();
        sortedMoves.sort(Comparator.comparingInt(Move::getMoveNumber));

        return sortedMoves;
    }
}
