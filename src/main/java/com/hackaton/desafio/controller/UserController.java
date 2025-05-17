package com.hackaton.desafio.controller;

import com.hackaton.desafio.dto.userDTO.UserRequest;
import com.hackaton.desafio.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("teste/user")
public class UserController {

    private final UserRepository repository;
    private final UserService userService;


    public UserController(UserRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(UserRequest user){
        return userService.login(user);
    }

}
