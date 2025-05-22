package com.hackaton.desafio.dto.enterpriseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record EnterpriseResponse (
        @Schema(description = "Enterprise ID", example = "1")
        Long id,
        @Schema(description = "Enterprise name", example = "Benefix")
        String name,
        @Schema(description = "Enterprise CNPJ", example = "12345678000195")
        String cnpj
){
}
