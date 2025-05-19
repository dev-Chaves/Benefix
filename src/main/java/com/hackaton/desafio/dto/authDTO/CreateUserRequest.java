package com.hackaton.desafio.dto.authDTO;

public record CreateUserRequest(
        String name,
        String password,
        Long enterprise
        ) {
}
