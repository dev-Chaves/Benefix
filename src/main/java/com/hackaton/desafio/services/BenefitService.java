package com.hackaton.desafio.services;

import com.hackaton.desafio.dto.benefitDTO.BenefitResponse;
import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.entity.UserEntity;
import com.hackaton.desafio.repository.BenefitRepository;
import com.hackaton.desafio.repository.EnterpriseRepository;
import com.hackaton.desafio.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BenefitService {

    private final BenefitRepository benefitRepository;
    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;

    public BenefitService(BenefitRepository benefitRepository, UserRepository userRepository, EnterpriseRepository enterpriseRepository) {
        this.benefitRepository = benefitRepository;
        this.userRepository = userRepository;
        this.enterpriseRepository = enterpriseRepository;
    }

    public ResponseEntity<?> getBenefitsByEnterprise() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof String username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        UserEntity user = userRepository.findByName(username).orElseThrow(()->
                new UsernameNotFoundException("User not found"));

        EnterpriseEntity enterprise = user.getEnterprise();

        List<BenefitEntity> benefits = benefitRepository.findByEnterpriseId(user.getEnterprise().getId());

        List<BenefitResponse> response = benefits.stream()
                .map(b -> new BenefitResponse(b.getId(), b.getDescription(), b.getSupplierEnterprise().getEnterprise()))
                .toList();

        return ResponseEntity.ok(response);
    }

}
