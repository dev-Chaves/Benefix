package com.hackaton.desafio.dto.benefitDTO;

import com.hackaton.desafio.entity.Enum.BenefitCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public record BenefitResponse(
        @Schema(description = "ID of the Benefit", example = "1")
        Long id,
        @Schema(description = "Name of the Benefit", example = "Desconnt ")
        String description,
        @Schema(description = "Supplier Enterprise ID", example = "1")
        String nameSupplierEnterprise,
        @Schema(description = "Benefit Category", example = "HEALTH")
        BenefitCategory category
) {
}
