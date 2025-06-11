package com.hackaton.desafio.dto.partnershipDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record PartnershipResponse(
        @Schema(description = "Id of the partnership", example = "1")
        Long id,
        @Schema(description = "Name of the enterprise that is the consumer enterprise", example = "Supplier Enterprise")
        String namePartnershipEnterprise,
        @Schema(description = "Name of the enterprise that is the supplier enterprise", example = "Consumer Enterprise")
        String nameSupplierEnterprise
) {
}
