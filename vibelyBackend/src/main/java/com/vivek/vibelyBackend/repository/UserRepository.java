package com.vivek.vibelyBackend.repository;

import com.vivek.vibelyBackend.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Integer> {

    boolean existsByUsername(String username);

    Optional<AppUser> findByUsername(String username);

    @Query("SELECT top 10u FROM User u ORDER BY u.wins DESC, u.losses ASC")
    List<AppUser> findAllOrderedByScore();

}
