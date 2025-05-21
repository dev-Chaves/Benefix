package com.hackaton.desafio.controller;

import com.hackaton.desafio.dto.benefitDTO.BenefitResponse;
import com.hackaton.desafio.dto.enterpriseDTO.EnterpriseResponse;
import com.hackaton.desafio.dto.userDTO.UserResponse;
import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.repository.BenefitRepository;
import com.hackaton.desafio.repository.EnterpriseRepository;
import com.hackaton.desafio.repository.UserRepository;
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
    private final UserRepository userRepository;


    public TesteController(BenefitRepository benefitRepository, EnterpriseRepository enterpriseRepository, UserRepository userRepository) {
        this.benefitRepository = benefitRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("enterprise")
    private List<EnterpriseResponse> enterprises(){
        return enterpriseRepository.findAll().stream().map(enterprise -> new EnterpriseResponse(enterprise.getId(), enterprise.getEnterprise(), enterprise.getCnpj())).toList();
    }

    @GetMapping("users")
    private List<UserResponse> users(){
        return userRepository.findAll().stream().map(user -> new UserResponse(user.getName(), user.getRole(), user.getEnterprise().getEnterprise())).toList();
    }

    @GetMapping("benefit")
    private List<BenefitResponse> benefitEntities(){
        return benefitRepository.findAll().stream().map(benefit -> new BenefitResponse(benefit.getId(), benefit.getDescription(), benefit.getSupplierEnterprise().getEnterprise(), benefit.getCategory())).toList();
    }
}
