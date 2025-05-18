package com.hackaton.desafio.dto.partnershipDTO;

public record PartnershipRequest(
        Long supplierEnterpriseId,
        Long consumerEnterpriseId
) {
}
