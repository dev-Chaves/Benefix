package com.hackaton.desafio.util;

import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.entity.Enum.BenefitCategory;
import com.hackaton.desafio.entity.PartnershipEntity;
import com.hackaton.desafio.entity.UserEntity;
import com.hackaton.desafio.repository.BenefitRepository;
import com.hackaton.desafio.repository.EnterpriseRepository;
import com.hackaton.desafio.repository.PartnershipRepository;
import com.hackaton.desafio.repository.UserRepository;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EnterpriseRepository enterpriseRepository;
    private final UserRepository userRepository;
    private final BenefitRepository benefitRepository;
    private final PartnershipRepository partnershipRepository;

    public DataInitializer(EnterpriseRepository enterpriseRepository, UserRepository userRepository, BenefitRepository benefitRepository, PartnershipRepository partnershipRepository) {
        this.enterpriseRepository = enterpriseRepository;
        this.userRepository = userRepository;
        this.benefitRepository = benefitRepository;
        this.partnershipRepository = partnershipRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        try {
            if (enterpriseRepository.count() == 0) {

                EnterpriseEntity empresa1 = new EnterpriseEntity("Empresa X", "11111111000100", LocalDateTime.now());
                EnterpriseEntity empresa2 = new EnterpriseEntity("Empresa Y", "22222222000100", LocalDateTime.now());
                EnterpriseEntity empresa3 = new EnterpriseEntity("Empresa Z", "33333333000100", LocalDateTime.now());

                enterpriseRepository.saveAll(List.of(empresa1, empresa2, empresa3));
                System.out.println("✅ Enterprises create with successs");

                UserEntity jaime = new UserEntity("jaime", "senha123", empresa1);
                UserEntity luna = new UserEntity("luna", "senha123", empresa2);
                UserEntity jao = new UserEntity("jao", "senha123", empresa3);

                userRepository.saveAll(List.of(jaime, luna, jao));
                System.out.println("✅ Users created with success");

                BenefitEntity beneficio1 = new BenefitEntity("Vale Alimentação X", empresa1, BenefitCategory.MARKET);
                BenefitEntity beneficio2 = new BenefitEntity("Plano de Saúde Y", empresa2, BenefitCategory.DRUGSTORE);
                BenefitEntity beneficio3 = new BenefitEntity("GymPass Z", empresa3, BenefitCategory.GYM);

                benefitRepository.saveAll(List.of(beneficio1, beneficio2, beneficio3));
                System.out.println("✅ Benefits created with success");

                PartnershipEntity parceria1 = new PartnershipEntity(empresa1, empresa2);
                PartnershipEntity parceria2 = new PartnershipEntity(empresa2, empresa3);
                PartnershipEntity parceria3 = new PartnershipEntity(empresa3, empresa1);

                partnershipRepository.saveAll(List.of(parceria1, parceria2, parceria3));
                System.out.println("✅ Partnership created with success");

            } else {
                System.out.println("ℹ️ Data already exist.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error : " + e.getMessage());
            e.printStackTrace();
        }
    }


}
