package com.hackaton.desafio.services;

import com.hackaton.desafio.dto.benefitDTO.BenefitRequest;
import com.hackaton.desafio.dto.benefitDTO.BenefitResponse;
import com.hackaton.desafio.dto.enterpriseDTO.EnterpriseRequest;
import com.hackaton.desafio.dto.enterpriseDTO.EnterpriseResponse;
import com.hackaton.desafio.dto.partnershipDTO.PartnershipRequest;
import com.hackaton.desafio.dto.userDTO.UserRequest;
import com.hackaton.desafio.dto.userDTO.UserResponse;
import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.entity.PartnershipEntity;
import com.hackaton.desafio.entity.Role.Role;
import com.hackaton.desafio.entity.UserEntity;
import com.hackaton.desafio.repository.BenefitRepository;
import com.hackaton.desafio.repository.EnterpriseRepository;
import com.hackaton.desafio.repository.PartnershipRepository;
import com.hackaton.desafio.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final PartnershipRepository partnershipRepository;
    private final PasswordEncoder passwordEncoder;
    private final BenefitRepository benefitRepository;

    public AdminService(UserRepository userRepository, EnterpriseRepository enterpriseRepository, PartnershipRepository partnershipRepository, PasswordEncoder passwordEncoder, BenefitRepository benefitRepository) {
        this.userRepository = userRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.partnershipRepository = partnershipRepository;
        this.passwordEncoder = passwordEncoder;
        this.benefitRepository = benefitRepository;
    }

    @Transactional
    public ResponseEntity<?> createUser(UserRequest userRequest) {

        EnterpriseEntity enterprise = enterpriseRepository.findById(userRequest.enterprise())
                .orElseThrow(() -> new EntityNotFoundException("Enterprise not found"));


        if(userRequest == null
                || userRequest.name() == null || userRequest.name().isBlank()
                || userRequest.password() == null || userRequest.password().isBlank()){
            return ResponseEntity.badRequest().body("Invalid user data");
        }

        var user = new UserEntity();

        user.setName(userRequest.name());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setEnterprise(enterprise);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        UserResponse userResponse = new UserResponse(user.getName(), user.getRole(), user.getEnterprise().getEnterprise());

        return ResponseEntity.ok(userResponse);
    }

    @Transactional
    public ResponseEntity<?> createBenefit(BenefitRequest benefityRequest) {

        EnterpriseEntity enterprise = enterpriseRepository.findById(benefityRequest.supplierEnterpriseId()).orElseThrow(() -> new EntityNotFoundException("Enterprise not found"));

        if(benefityRequest.supplierEnterpriseId() == null || benefityRequest.description() == null){
            return ResponseEntity.badRequest().body("Invalid benefit data");
        }

        BenefitEntity benefit = new BenefitEntity();

        benefit.setDescription(benefityRequest.description());
        benefit.setSupplierEnterprise(enterprise);

        benefitRepository.save(benefit);

        return ResponseEntity.ok(new BenefitResponse(
                benefit.getDescription(),
                benefit.getSupplierEnterprise().getEnterprise()
        ));
    }

    @Transactional
    public ResponseEntity<?> createEnterprise(EnterpriseRequest enterpriseRequest) {

        if(enterpriseRequest.name().isEmpty() || enterpriseRequest.cnpj().isEmpty()){
            return ResponseEntity.badRequest().body("Invalid enterprise data");
        }

        EnterpriseEntity enterprise = new EnterpriseEntity();

        enterprise.setEnterprise(enterpriseRequest.name());
        enterprise.setCnpj(enterpriseRequest.cnpj());
        enterprise.setCreatedAt(LocalDateTime.now());

        enterpriseRepository.save(enterprise);

        return ResponseEntity.ok(new EnterpriseResponse(enterprise.getEnterprise(), enterprise.getCnpj()));
    }

    public ResponseEntity<?> createPartnership(@Valid PartnershipRequest partnershipRequest) {

        EnterpriseEntity enterpriseSupplier = enterpriseRepository.findById(partnershipRequest.supplierEnterpriseId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier enterprise not found"));

        EnterpriseEntity enterpriseProvider = enterpriseRepository.findById(partnershipRequest.consumerEnterpriseId())
                .orElseThrow(() -> new EntityNotFoundException("Consumer enterprise not found"));

        if(partnershipRequest.supplierEnterpriseId() == null || partnershipRequest.consumerEnterpriseId() == null){
            return ResponseEntity.badRequest().body("Invalid partnership data");
        }

        if(partnershipRepository.existsBySupplierEnterpriseAndConsumerEnterprise(enterpriseSupplier, enterpriseProvider)){
            return ResponseEntity.badRequest().body("Partnership already exists");
        }

        PartnershipEntity partnership = new PartnershipEntity();

        return null;
    }

    private final Role GetRole(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity user;

        if (principal instanceof UserEntity) {
            user = (UserEntity) principal;
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return user.getRole();
    }

}
