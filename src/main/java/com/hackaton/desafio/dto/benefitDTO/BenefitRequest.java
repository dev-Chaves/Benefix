package com.hackaton.desafio.dto.benefitDTO;

import com.hackaton.desafio.entity.Enum.BenefitCategory;

public record BenefitRequest(
        String description,
        Long supplierEnterpriseId,
        BenefitCategory category

) {
}
