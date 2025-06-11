package com.hackaton.desafio.dto.IA;

import io.swagger.v3.oas.annotations.media.Schema;

public record DoubtRequest(
        @Schema(description = "The question to be asked", example = "How many meetings do I have today?")
        String question
        ) {
}
