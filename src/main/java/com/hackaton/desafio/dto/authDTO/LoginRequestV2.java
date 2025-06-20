package com.hackaton.desafio.dto.authDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequestV2(
        @Schema(description = "CPF of Collaborator", example = "12345678909")
        String cpf,
        @Schema(description = "Password of Collaborator", example = "123456")
        String password) {

}
