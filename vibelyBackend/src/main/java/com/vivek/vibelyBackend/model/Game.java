package com.vivek.vibelyBackend.model;

import com.vivek.vibelyBackend.model.enums.GameStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_x_id")
    private AppUser playerX;

    @ManyToOne
    @JoinColumn(name = "player_o_id")
    private AppUser playerO;

    @Column(length = 9)
    private String board = "         "; // 9 spaces

    @Enumerated(EnumType.STRING)
    private GameStatus status = GameStatus.IN_PROGRESS;

    private String currentTurn = "X";

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private AppUser winner;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;
}
