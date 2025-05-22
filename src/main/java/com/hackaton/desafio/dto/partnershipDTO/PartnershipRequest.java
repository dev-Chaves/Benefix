package com.hackaton.desafio.dto.partnershipDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record PartnershipRequest(
        @Schema(description = "Id of  supplier enterprise", example = "1")
        Long supplierEnterpriseId,
        @Schema(description = "Id of consumer enterprise", example = "2")
        Long consumerEnterpriseId
) {
}
