package com.hackaton.desafio.services;

import com.hackaton.desafio.repository.BenefitRepository;
import com.hackaton.desafio.repository.EnterpriseRepository;
import com.hackaton.desafio.repository.UserRepository;
import org.springframework.stereotype.Service;

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
}
