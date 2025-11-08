package com.vivek.vibelyBackend.config;

import jakarta.servlet.FilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public FilterChain filterChain(HttpSecurity http) throws Exception {
        return http.build();
    }
}
