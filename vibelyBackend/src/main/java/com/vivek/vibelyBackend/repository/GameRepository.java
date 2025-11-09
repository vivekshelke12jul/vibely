package com.vivek.vibelyBackend.repository;

import com.vivek.vibelyBackend.model.AppUser;
import com.vivek.vibelyBackend.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByPlayerXOrPlayerO(AppUser playerX, AppUser playerO);

    List<Game> findByPlayerXOrPlayerOOrderByCreatedAtDesc(AppUser user, AppUser user1);
}
