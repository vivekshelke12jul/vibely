package com.vivek.vibelyBackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class publicController {

    @GetMapping("/home")
    public String home(){
        return "home sweet home";
    }
}
