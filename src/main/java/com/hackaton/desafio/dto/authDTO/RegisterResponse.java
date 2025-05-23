package com.hackaton.desafio.dto.authDTO;

import com.hackaton.desafio.entity.Role.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterResponse(
        @Schema(description = "Username of Collaborator", example = "João Vitor")
        String name,
        @Schema(description = "Role of Collaborator", example = "USER")
        Role role
) {
}
