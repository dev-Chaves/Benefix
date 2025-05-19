package com.hackaton.desafio.dto.authDTO;

public record RegisterDTO(
        String name,
        String password,
        Long enterprise,
        String token
        ) {
}
