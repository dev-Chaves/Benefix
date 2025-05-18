package com.hackaton.desafio.services;

import com.hackaton.desafio.dto.userDTO.UserRequest;
import com.hackaton.desafio.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> login(UserRequest user) {
        // Implement the login logic here
        return ResponseEntity.ok("Login successful");
    }

}
