package com.hackaton.desafio.dto.benefitDTO;

import com.hackaton.desafio.entity.Enum.BenefitCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public record BenefitRequest(
        @Schema(description = "Name of the Benefit", example = "Gym")
        String description,
        @Schema(description = "Supplier Enterprise ID", example = "1")
        Long supplierEnterpriseId,
        @Schema(description = "Benefit Category", example = "HEALTH")
        BenefitCategory category

) {
}
