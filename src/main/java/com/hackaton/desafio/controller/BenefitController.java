package com.hackaton.desafio.controller;

import com.hackaton.desafio.dto.benefitDTO.BenefitResponse;
import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.Enum.BenefitCategory;
import com.hackaton.desafio.repository.BenefitRepository;
import com.hackaton.desafio.services.BenefitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/benefits")
@Tag(name = "Benefits", description = "Endpoints for managing benefits")
public class BenefitController {

    private final BenefitService benefitService;
    public BenefitController( BenefitService benefitService) {
        this.benefitService = benefitService;
    }

    @GetMapping("benefits-by-enterprise")
    @Operation(summary = "Get all benefits by enterprise")
    public ResponseEntity<?> getBenefitsByEnterprise() {
        return benefitService.getBenefitsByEnterprise();
    }

    @GetMapping("user-benefits")
    @Operation(summary = "Get all benefits by user")
    public ResponseEntity<?> getBenefitOfPartneship() {
        return benefitService.getBenefitOfPartneship();
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get all benefits by category")
    public ResponseEntity<List<BenefitResponse>> getByCategory(@PathVariable String category) {
        return benefitService.getBenefitsByCategory(category);
    }

}
