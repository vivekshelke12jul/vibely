package com.vivek.vibelyBackend.exchanges.response;

import com.vivek.vibelyBackend.model.enums.GameStatus;
import lombok.Data;

@Data
public class GameResponse {
    private Long id;
    private String playerXUsername;
    private String playerOUsername;
    private String board;
    private GameStatus status;
    private String currentTurn;
    private String winnerUsername;
}
