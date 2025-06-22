package com.hackaton.desafio.dto.authDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "Username of Collaborator", example = "João Vitor")
        String name,
        @Schema(description = "CPF of Collaborator", example = "12345678909")
        String cpf,
        @Schema(description = "Password of Collaborator", example = "123456")
        String password) {

}
