package com.hackaton.desafio.services;

import com.hackaton.desafio.dto.authDTO.CreateUserRequest;
import com.hackaton.desafio.dto.benefitDTO.BenefitRequest;
import com.hackaton.desafio.dto.benefitDTO.BenefitResponse;
import com.hackaton.desafio.dto.enterpriseDTO.EnterpriseRequest;
import com.hackaton.desafio.dto.enterpriseDTO.EnterpriseResponse;
import com.hackaton.desafio.dto.partnershipDTO.PartnershipRequest;
import com.hackaton.desafio.dto.partnershipDTO.PartnershipResponse;
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
import com.hackaton.desafio.util.EncryptionUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final PartnershipRepository partnershipRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionUtil encryptionUtil;
    private final BenefitRepository benefitRepository;

    public AdminService(UserRepository userRepository, EnterpriseRepository enterpriseRepository, PartnershipRepository partnershipRepository, PasswordEncoder passwordEncoder, EncryptionUtil encryptionUtil, BenefitRepository benefitRepository) {
        this.userRepository = userRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.partnershipRepository = partnershipRepository;
        this.passwordEncoder = passwordEncoder;
        this.encryptionUtil = encryptionUtil;
        this.benefitRepository = benefitRepository;
    }

    @Transactional
    public ResponseEntity<?> createUser(CreateUserRequest userRequest) {

        EnterpriseEntity enterprise = enterpriseRepository.findById(userRequest.enterprise())
                .orElseThrow(() -> new EntityNotFoundException("Enterprise not found"));


        if(userRequest == null
                || userRequest.name() == null || userRequest.name().isBlank()
                || userRequest.password() == null || userRequest.password().isBlank() || userRequest.cpf() == null){
            return ResponseEntity.badRequest().body("Invalid user data");
        }

        var user = new UserEntity();

        user.setName(userRequest.name());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        try {
            user.setCpf(encryptionUtil.encrypt(userRequest.cpf()));
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
        user.setEnterprise(enterprise);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        UserResponse userResponse = new UserResponse(user.getName(), user.getRole(), user.getEnterprise().getEnterprise());

        return ResponseEntity.ok(userResponse);
    }

    @Transactional
    public ResponseEntity<?> createBenefit(BenefitRequest benefitRequest) {

        if(getRole() != Role.ADMIN){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: only admins can create benefits");
        }

        if(benefitRequest.supplierEnterpriseId() == null || benefitRequest.description() == null || benefitRequest.category() == null){
            return ResponseEntity.badRequest().body("Invalid benefit data");
        }

        EnterpriseEntity enterprise = enterpriseRepository.findById(benefitRequest.supplierEnterpriseId()).orElseThrow(() -> new EntityNotFoundException("Enterprise not found"));

        BenefitEntity benefit = new BenefitEntity();

        benefit.setDescription(benefitRequest.description());
        benefit.setSupplierEnterprise(enterprise);
        benefit.setCategory(benefitRequest.category());
        benefit.setCreatedAt(LocalDateTime.now());

        benefitRepository.save(benefit);

        return ResponseEntity.ok(new BenefitResponse(
                benefit.getId(),
                benefit.getDescription(),
                benefit.getSupplierEnterprise().getEnterprise(),
                benefit.getCategory()
        ));
    }

    @Transactional
    public ResponseEntity<?> createEnterprise(EnterpriseRequest enterpriseRequest) {

        if (getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: only admins can create enterprises");
        }

        if (enterpriseRequest.name().isEmpty() || enterpriseRequest.cnpj().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid enterprise data");
        }

        EnterpriseEntity enterprise = new EnterpriseEntity();
        enterprise.setEnterprise(enterpriseRequest.name());
        enterprise.setCnpj(enterpriseRequest.cnpj());
        enterprise.setCreatedAt(LocalDateTime.now());

        enterpriseRepository.save(enterprise);

        return ResponseEntity.ok(new EnterpriseResponse(enterprise.getId(), enterprise.getEnterprise(), enterprise.getCnpj()));
    }


    @Transactional
    public ResponseEntity<?> createPartnership(PartnershipRequest partnershipRequest){

        if(getRole() != Role.ADMIN){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: only admins can create partnerships");
        }

        if(partnershipRequest.consumerEnterpriseId() == null || partnershipRequest.supplierEnterpriseId() == null){
            return ResponseEntity.badRequest().body("Invalid partnership data");
        }

        EnterpriseEntity consumerEnterprise = enterpriseRepository.findById(partnershipRequest.consumerEnterpriseId())
                .orElseThrow(() -> new EntityNotFoundException("Consumer enterprise not found"));
        EnterpriseEntity supplierEnterprise = enterpriseRepository.findById(partnershipRequest.supplierEnterpriseId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier enterprise not found"));

        PartnershipEntity partnership = new PartnershipEntity();

        Long id1 = consumerEnterprise.getId();
        Long id2 = supplierEnterprise.getId();

        if(id1.equals(id2)){
            return ResponseEntity.badRequest().body("Consumer and supplier enterprises cannot be the same");
        }

        if(partnershipRepository.existsBySupplierEnterpriseAndConsumerEnterprise(consumerEnterprise, supplierEnterprise)){
            return ResponseEntity.badRequest().body("Partnership already exists");
        }

        partnership.setConsumerEnterprise(consumerEnterprise);
        partnership.setSupplierEnterprise(supplierEnterprise);
        partnershipRepository.save(partnership);

        return ResponseEntity.ok(new PartnershipResponse(
                partnership.getId(),
                partnership.getConsumerEnterprise().getEnterprise(),
                partnership.getSupplierEnterprise().getEnterprise()
        ));

    }

    private static Role getRole() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserEntity user) {
            return user.getRole();
        }

        throw new RuntimeException("Usuário não autenticado");
    }

}
