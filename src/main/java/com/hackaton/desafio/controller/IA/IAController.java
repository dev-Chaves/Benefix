package com.hackaton.desafio.controller.IA;

import com.hackaton.desafio.dto.IA.DoubtRequest;
import com.hackaton.desafio.dto.IA.DoubtResponse;
import com.hackaton.desafio.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ia")
public class IAController {

    private final UserService userService;

    public IAController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("doubt")
    public ResponseEntity<DoubtResponse> createDoubt(@Valid @RequestBody DoubtRequest doubt){

        return userService.createDoubt(doubt);

    }

}
