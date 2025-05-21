package com.hackaton.desafio.dto.benefitDTO;

import com.hackaton.desafio.entity.Enum.BenefitCategory;

public record BenefitResponse(
        Long id,
        String description,
        String nameSupplierEnterprise,
        BenefitCategory category
) {
}
