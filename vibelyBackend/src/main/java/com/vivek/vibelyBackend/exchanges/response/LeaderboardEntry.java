package com.vivek.vibelyBackend.exchanges.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardEntry {
    private String username;
    private Integer wins;
    private Integer losses;
    private Integer draws;
    private Integer totalGames;
}