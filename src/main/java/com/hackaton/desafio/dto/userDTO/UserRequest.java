package com.hackaton.desafio.dto.userDTO;

import com.hackaton.desafio.entity.Role.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserRequest(
        @Schema(description = "User's name", example = "John Doe")
        String name,
        @Schema(description = "User's password", example = "password123")
        String password,
        @Schema(description = "CPF of the user", example = "1234567909")
        String cpf,
        @Schema(description = "Enterprise ID of the user", example = "1")
        Long enterprise,
        @Schema(description = "User's role", example = "USER")
        Role role
) {
}
