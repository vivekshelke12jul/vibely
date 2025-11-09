package com.vivek.vibelyBackend.service;

import com.vivek.vibelyBackend.exchanges.request.MoveRequest;
import com.vivek.vibelyBackend.exchanges.response.GameResponse;
import com.vivek.vibelyBackend.model.AppUser;
import com.vivek.vibelyBackend.model.Game;
import com.vivek.vibelyBackend.model.enums.GameStatus;
import com.vivek.vibelyBackend.repository.GameRepository;
import com.vivek.vibelyBackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public GameResponse createGame(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Game game = new Game();
        game.setPlayerX(user);
        game = gameRepository.save(game);

        return mapToResponse(game);
    }

    @Transactional
    public GameResponse joinGame(Long gameId, String username) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (game.getPlayerO() != null) {
            throw new RuntimeException("Game already has two players");
        }

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("AppUser not found"));

        if (game.getPlayerX().getId().equals(user.getId())) {
            throw new RuntimeException("Cannot play against yourself");
        }

        game.setPlayerO(user);
        game = gameRepository.save(game);

        return mapToResponse(game);
    }

    @Transactional
    public GameResponse makeMove(Long gameId, String username, MoveRequest moveRequest) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new RuntimeException("Game is already finished");
        }

        if (game.getPlayerO() == null) {
            throw new RuntimeException("Waiting for second player to join");
        }

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String symbol = getPlayerSymbol(game, user);
        if (!symbol.equals(game.getCurrentTurn())) {
            throw new RuntimeException("Not your turn");
        }

        int pos = moveRequest.getPosition();
        if (pos < 0 || pos > 8) {
            throw new RuntimeException("Invalid position");
        }

        String board = game.getBoard();
        if (board.charAt(pos) != ' ') {
            throw new RuntimeException("Position already occupied");
        }

        char[] boardArray = board.toCharArray();
        boardArray[pos] = symbol.charAt(0);
        game.setBoard(new String(boardArray));

        GameStatus status = checkGameStatus(game.getBoard());
        game.setStatus(status);

        if (status != GameStatus.IN_PROGRESS) {
            game.setFinishedAt(LocalDateTime.now());
            updatePlayerStats(game);
        } else {
            game.setCurrentTurn(game.getCurrentTurn().equals("X") ? "O" : "X");
        }

        game = gameRepository.save(game);
        return mapToResponse(game);
    }

    public GameResponse getGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        return mapToResponse(game);
    }

    public List<GameResponse> getAppUserGames(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("AppUser not found"));

        return gameRepository.findByPlayerXOrPlayerOOrderByCreatedAtDesc(user, user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private String getPlayerSymbol(Game game, AppUser user) {
        if (game.getPlayerX().getId().equals(user.getId())) return "X";
        if (game.getPlayerO() != null && game.getPlayerO().getId().equals(user.getId())) return "O";
        throw new RuntimeException("AppUser is not a player in this game");
    }

    private GameStatus checkGameStatus(String board) {
        int[][] winPatterns = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8},
                {0, 4, 8},
                {2, 4, 6}
        };

        for (int[] pattern : winPatterns) {
            char a = board.charAt(pattern[0]);
            char b = board.charAt(pattern[1]);
            char c = board.charAt(pattern[2]);

            if (a != ' ' && a == b && b == c) {
                return a == 'X' ? GameStatus.X_WON : GameStatus.O_WON;
            }
        }

        if (board.indexOf(' ') == -1) {
            return GameStatus.DRAW;
        }

        return GameStatus.IN_PROGRESS;
    }

    private void updatePlayerStats(Game game) {
        if (game.getStatus() == GameStatus.X_WON) {
            game.setWinner(game.getPlayerX());
            incrementWins(game.getPlayerX());
            incrementLosses(game.getPlayerO());
        } else if (game.getStatus() == GameStatus.O_WON) {
            game.setWinner(game.getPlayerO());
            incrementWins(game.getPlayerO());
            incrementLosses(game.getPlayerX());
        } else if (game.getStatus() == GameStatus.DRAW) {
            incrementDraws(game.getPlayerX());
            incrementDraws(game.getPlayerO());
        }
    }

    private void incrementWins(AppUser user) {
        user.setWins(user.getWins() + 1);
        userRepository.save(user);
    }

    private void incrementLosses(AppUser user) {
        user.setLosses(user.getLosses() + 1);
        userRepository.save(user);
    }

    private void incrementDraws(AppUser user) {
        user.setDraws(user.getDraws() + 1);
        userRepository.save(user);
    }

    private GameResponse mapToResponse(Game game) {
        GameResponse response = new GameResponse();
        response.setId(game.getId());
        response.setPlayerXUsername(game.getPlayerX().getUsername());
        response.setPlayerOUsername(game.getPlayerO() != null ? game.getPlayerO().getUsername() : null);
        response.setBoard(game.getBoard());
        response.setStatus(game.getStatus());
        response.setCurrentTurn(game.getCurrentTurn());
        response.setWinnerUsername(game.getWinner() != null ? game.getWinner().getUsername() : null);
        return response;
    }
}
