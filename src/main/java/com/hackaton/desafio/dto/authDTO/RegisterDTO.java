package com.hackaton.desafio.dto.authDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterDTO(
        @Schema(description = "Username of Collaborator", example = "João Vitor")
        String name,
        @Schema(description = "Password of Collaborator", example = "123456")
        String password,
        @Schema(description = "Enterprise ID of Collaborator", example = "1")
        Long enterprise,
        @Schema(description = "Bearer Token of Collaborator", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
        String token
        ) {
}
