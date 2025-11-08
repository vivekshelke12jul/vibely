package com.vivek.vibelyBackend.controller;

import com.vivek.vibelyBackend.exchanges.request.LoginRequest;
import com.vivek.vibelyBackend.exchanges.request.RegisterRequest;
import com.vivek.vibelyBackend.exchanges.response.AuthResponse;
import com.vivek.vibelyBackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class publicController {

    @Autowired
    AuthService authService;

    @GetMapping
    public String home(){
        return "public info";
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
