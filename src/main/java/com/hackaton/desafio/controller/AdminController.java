package com.hackaton.desafio.controller;

import com.hackaton.desafio.dto.authDTO.CreateUserRequest;
import com.hackaton.desafio.dto.benefitDTO.BenefitRequest;
import com.hackaton.desafio.dto.enterpriseDTO.EnterpriseRequest;
import com.hackaton.desafio.dto.partnershipDTO.PartnershipRequest;
import com.hackaton.desafio.repository.UserRepository;
import com.hackaton.desafio.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Endpoints for managing ADMIN operations")
public class AdminController {

    private final UserRepository repository;
    private final AdminService adminService;

    public AdminController(UserRepository repository, AdminService adminService) {
        this.repository = repository;
        this.adminService = adminService;
    }

    @Operation(summary = "Create a collaborator")
    @PostMapping("/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest userRequest){
        return adminService.createUser(userRequest);
    }

    @Operation(summary = "Create a benefit")
    @PostMapping("/benefit")
    public ResponseEntity<?> createBenefit(@Valid @RequestBody BenefitRequest benefitRequest){
        return adminService.createBenefit(benefitRequest);
    }

    @Operation(summary = "Create an enterprise")
    @PostMapping("/enterprise")
    public ResponseEntity<?> createEnterprise(@Valid @RequestBody EnterpriseRequest enterprise){
        return adminService.createEnterprise(enterprise);
    }

    @Operation(summary = "Create a partnership")
    @PostMapping("/partnership")
    public ResponseEntity<?> createPartnership(@Valid @RequestBody PartnershipRequest partnership){
        return adminService.createPartnership(partnership);
    }


}
