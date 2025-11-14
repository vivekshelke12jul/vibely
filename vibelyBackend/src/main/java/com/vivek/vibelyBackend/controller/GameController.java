package com.vivek.vibelyBackend.controller;

import com.vivek.vibelyBackend.exchanges.request.MoveRequest;
import com.vivek.vibelyBackend.exchanges.response.GameResponse;
import com.vivek.vibelyBackend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    public ResponseEntity<GameResponse> createGame(Authentication auth) {
        return ResponseEntity.ok(gameService.createGame(auth.getName()));
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<GameResponse> joinGame(@PathVariable Long gameId, Authentication auth) {
        return ResponseEntity.ok(gameService.joinGame(gameId, auth.getName()));
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<GameResponse> makeMove(@PathVariable Long gameId,
                                                 @RequestBody MoveRequest moveRequest,
                                                 Authentication auth) {
        return ResponseEntity.ok(gameService.makeMove(gameId, auth.getName(), moveRequest));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> getGame(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.getGame(gameId));
    }

    @GetMapping
    public ResponseEntity<List<GameResponse>> getUserGames(Authentication auth) {
        return ResponseEntity.ok(gameService.getUserGames(auth.getName()));
    }
}
