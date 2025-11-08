package com.vivek.vibelyBackend.model;

import com.vivek.vibelyBackend.model.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue
    private Integer id;
    private String username;
    private String password;
    private Role role;

    public AppUser(String username, String password, Role role){
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
