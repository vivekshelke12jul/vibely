package com.vivek.vibelyBackend.repository;

import com.vivek.vibelyBackend.model.AppUser;
import com.vivek.vibelyBackend.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findAllByPlayerXOrPlayerO(AppUser playerX, AppUser playerO);
    @Query("SELECT g FROM Game g WHERE g.playerX = :user OR g.playerO = :user ORDER BY g.createdAt DESC")
    List<Game> findLatestGames(AppUser user);

}
