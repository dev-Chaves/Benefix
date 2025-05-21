package com.hackaton.desafio.controller;

import com.hackaton.desafio.dto.benefitDTO.BenefitResponse;
import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.Enum.BenefitCategory;
import com.hackaton.desafio.repository.BenefitRepository;
import com.hackaton.desafio.services.BenefitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/benefits")
public class BenefitController {

    private final BenefitRepository benefitsRepository;
    private final BenefitService benefitService;
    private final BenefitRepository benefitRepository;

    public BenefitController(BenefitRepository benefitRepository, BenefitService benefitService, BenefitRepository benefitRepository1) {
        this.benefitsRepository = benefitRepository;
        this.benefitService = benefitService;
        this.benefitRepository = benefitRepository1;
    }

    @GetMapping("benefits-by-enterprise")
    public ResponseEntity<?> getBenefitsByEnterprise() {
        return benefitService.getBenefitsByEnterprise();
    }

    @GetMapping("user-benefits")
    public ResponseEntity<?> getBenefitOfPartneship() {
        return benefitService.getBenefitOfPartneship();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<BenefitResponse>> getByCategory(@PathVariable String category) {
        return benefitService.getBenefitsByCategory(category);
    }

}
