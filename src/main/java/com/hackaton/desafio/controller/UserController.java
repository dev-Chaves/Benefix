package com.hackaton.desafio.controller;

import com.hackaton.desafio.dto.authDTO.LoginRequest;
import com.hackaton.desafio.dto.authDTO.RegisterDTO;
import com.hackaton.desafio.repository.UserRepository;
import com.hackaton.desafio.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserRepository repository;
    private final UserService userService;


    public UserController(UserRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest user){
        return userService.login(user);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO user){
        return userService.register(user);
    }

}
