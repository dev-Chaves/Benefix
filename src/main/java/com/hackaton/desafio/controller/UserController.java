package com.hackaton.desafio.controller;

import com.hackaton.desafio.dto.authDTO.LoginRequest;
import com.hackaton.desafio.dto.authDTO.RegisterDTO;
import com.hackaton.desafio.repository.UserRepository;
import com.hackaton.desafio.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@Tag(name = "User", description = "Endpoints for managing Users of the system")
public class UserController {

    private final UserRepository repository;
    private final UserService userService;


    public UserController(UserRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Operation(summary = "Logs in the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login effectuated"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest user){
        return userService.login(user);
    }

    @Operation(summary = "Register the ADMIN in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register effectuated")
    })
    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO user){
        return userService.register(user);
    }

}
