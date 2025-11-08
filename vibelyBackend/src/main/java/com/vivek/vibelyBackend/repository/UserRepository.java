package com.vivek.vibelyBackend.repository;

import com.vivek.vibelyBackend.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Integer> {
    AppUser findByUsername(String username);
}
