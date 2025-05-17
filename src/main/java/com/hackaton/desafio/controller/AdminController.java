package com.hackaton.desafio.controller;

import com.hackaton.desafio.dto.benefitDTO.BenefitRequest;
import com.hackaton.desafio.dto.enterpriseDTO.EnterpriseRequest;
import com.hackaton.desafio.dto.partnershipDTO.PartnershipRequest;
import com.hackaton.desafio.dto.userDTO.UserRequest;
import com.hackaton.desafio.repository.UserRepository;
import com.hackaton.desafio.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("teste/admin")
public class AdminController {

    private final UserRepository repository;
    private final AdminService adminService;

    public AdminController(UserRepository repository, AdminService adminService) {
        this.repository = repository;
        this.adminService = adminService;
    }

    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest){
        return adminService.createUser(userRequest);
    }

    public ResponseEntity<?> createBenefit(@RequestBody @Valid BenefitRequest benefitRequest){
        return adminService.createBenefit(benefitRequest);
    }

    public ResponseEntity<?> createEnterprise(@RequestBody @Valid EnterpriseRequest enterprise){
        return adminService.createEnterprise(enterprise);
    }

    public ResponseEntity<?> createPartnership(@RequestBody @Valid PartnershipRequest partnership){
        return adminService.createPartnership(partnership);
    }



    public ResponseEntity<?> updateBenefit(@RequestBody @Valid BenefitRequest benefitRequest){
        return adminService.updateBenefit(benefitRequest);
    }

}
