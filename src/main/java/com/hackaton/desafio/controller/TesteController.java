package com.hackaton.desafio.controller;

import com.hackaton.desafio.dto.enterpriseDTO.EnterpriseResponse;
import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.repository.BenefitRepository;
import com.hackaton.desafio.repository.EnterpriseRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("user")
public class TesteController {

    private final BenefitRepository benefitRepository;
    private final EnterpriseRepository enterpriseRepository;


    public TesteController(BenefitRepository benefitRepository, EnterpriseRepository enterpriseRepository) {
        this.benefitRepository = benefitRepository;
        this.enterpriseRepository = enterpriseRepository;
    }

    @GetMapping("enterprise")
    private List<EnterpriseResponse> enterprises(){
        return enterpriseRepository.findAll().stream().map(enterprise -> new EnterpriseResponse(enterprise.getId(), enterprise.getEnterprise(), enterprise.getCnpj())).toList();
    }

    @GetMapping("benefit")
    private List<BenefitEntity> benefitEntities(){



        return benefitRepository.findAll();
    }
}
