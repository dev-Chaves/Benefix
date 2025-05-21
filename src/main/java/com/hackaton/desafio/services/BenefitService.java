package com.hackaton.desafio.services;

import com.hackaton.desafio.dto.benefitDTO.BenefitResponse;
import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.entity.PartnershipEntity;
import com.hackaton.desafio.entity.UserEntity;
import com.hackaton.desafio.repository.BenefitRepository;
import com.hackaton.desafio.repository.EnterpriseRepository;
import com.hackaton.desafio.repository.PartnershipRepository;
import com.hackaton.desafio.repository.UserRepository;
import com.hackaton.desafio.util.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BenefitService {

    private final BenefitRepository benefitRepository;
    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final AuthUtil authUtil;
    private final PartnershipRepository partnershipRepository;

    public BenefitService(BenefitRepository benefitRepository, UserRepository userRepository, EnterpriseRepository enterpriseRepository, AuthUtil authUtil, PartnershipRepository partnershipRepository) {
        this.benefitRepository = benefitRepository;
        this.userRepository = userRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.authUtil = authUtil;
        this.partnershipRepository = partnershipRepository;
    }

    public ResponseEntity<?> getBenefitsByEnterprise() {

        UserEntity user = AuthUtil.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

        EnterpriseEntity enterprise = user.getEnterprise();

        List<BenefitEntity> benefits = benefitRepository.findByEnterpriseId(user.getEnterprise().getId());

        List<BenefitResponse> response = benefits.stream()
                .map(b -> new BenefitResponse(b.getId(), b.getDescription(), b.getSupplierEnterprise().getEnterprise()))
                .toList();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getBenefitOfPartneship(){

        UserEntity user = AuthUtil.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

         EnterpriseEntity userEnterprise = enterpriseRepository.findById(user.getEnterprise().getId()).orElseThrow(()-> new RuntimeException("Enterprise not found"));

        List<PartnershipEntity> partnerships = partnershipRepository.findByEnterpriseId(user.getEnterprise().getId());

        List<Long> partnershipIds = partnerships.stream().map(p ->{
             if (Objects.equals(p.getConsumerEnterprise().getId(), user.getEnterprise().getId())){
                 return p.getSupplierEnterprise().getId();
             }else {
                 return p.getConsumerEnterprise().getId();
             }
         }).distinct().toList();

         List<BenefitEntity> benefits = benefitRepository.findBySupplierEnterprise_IdIn(partnershipIds);

         List<BenefitResponse> responses = benefits.stream().map( b -> new BenefitResponse(b.getId(),b.getDescription(),b.getSupplierEnterprise().getEnterprise())).toList();

         return ResponseEntity.ok(responses);


    }

}
