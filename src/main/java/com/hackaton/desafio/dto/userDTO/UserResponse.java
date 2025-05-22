package com.hackaton.desafio.dto.userDTO;

import com.hackaton.desafio.entity.Role.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
        @Schema(description = "User's name", example = "John Doe")
        String name,
        @Schema(description = "User's role", example = "USER")
        Role role,
        @Schema(description = "Name of the enterprise", example = "Acme Corp")
        String empresa
) {
}