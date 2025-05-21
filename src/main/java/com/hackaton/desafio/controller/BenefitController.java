package com.hackaton.desafio.controller;

import com.hackaton.desafio.repository.BenefitRepository;
import com.hackaton.desafio.services.BenefitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/benefits")
public class BenefitController {

    private final BenefitRepository benefitsRepository;
    private final BenefitService benefitService;

    public BenefitController(BenefitRepository benefitRepository, BenefitService benefitService) {
        this.benefitsRepository = benefitRepository;
        this.benefitService = benefitService;
    }

    @GetMapping("benefits-by-enterprise")
    public ResponseEntity<?> getBenefitsByEnterprise() {
        return benefitService.getBenefitsByEnterprise();
    }

    @GetMapping("user-benefits")
    public ResponseEntity<?> getBenefitOfPartneship() {
        return benefitService.getBenefitOfPartneship();
    }

}
