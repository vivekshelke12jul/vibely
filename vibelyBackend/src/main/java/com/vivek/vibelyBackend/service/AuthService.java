package com.vivek.vibelyBackend.service;

import com.vivek.vibelyBackend.exchanges.request.LoginRequest;
import com.vivek.vibelyBackend.exchanges.request.RegisterRequest;
import com.vivek.vibelyBackend.exchanges.response.AuthResponse;
import com.vivek.vibelyBackend.model.AppUser;
import com.vivek.vibelyBackend.model.enums.Role;
import com.vivek.vibelyBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        AppUser user = new AppUser(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                Role.USER
        );

        userRepository.save(user);

        return new AuthResponse("registration successful");

    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        return new AuthResponse("login successful");
    }

}
