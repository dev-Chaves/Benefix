package com.hackaton.desafio.dto.IA;

import io.swagger.v3.oas.annotations.media.Schema;

public record DoubtResponse(

        @Schema(description = "The question to be asked", example = "How many meetings do I have today?")
        String doubt,
        @Schema(description = "User who asked the question", example ="João")
        String user,
        @Schema(description = "The answer to the question", example = "You have 3 meetings today")
        Boolean answered


) {
}
