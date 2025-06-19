package com.hackaton.desafio.util;

import com.github.javafaker.Faker;
import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.entity.Enum.BenefitCategory;
import com.hackaton.desafio.entity.PartnershipEntity;
import com.hackaton.desafio.entity.UserEntity;
import com.hackaton.desafio.repository.BenefitRepository;
import com.hackaton.desafio.repository.EnterpriseRepository;
import com.hackaton.desafio.repository.PartnershipRepository;
import com.hackaton.desafio.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EnterpriseRepository enterpriseRepository;
    private final UserRepository userRepository;
    private final BenefitRepository benefitRepository;
    private final PartnershipRepository partnershipRepository;
    private final Faker faker = new Faker(Locale.of("pt", "BR"));
    private final PasswordEncoder passwordEncoder;
    private final EncryptionUtil encryptionUtil;

    public DataInitializer(EnterpriseRepository enterpriseRepository, UserRepository userRepository, BenefitRepository benefitRepository, PartnershipRepository partnershipRepository, PasswordEncoder passwordEncoder, EncryptionUtil encryptionUtil) {
        this.enterpriseRepository = enterpriseRepository;
        this.userRepository = userRepository;
        this.benefitRepository = benefitRepository;
        this.partnershipRepository = partnershipRepository;
        this.passwordEncoder = passwordEncoder;
        this.encryptionUtil = encryptionUtil;
    }

    private final Set<String> generatedCpfs = new HashSet<>();
    @Override
    @Transactional
    public void run(String... args) {
        try {
            // Verifica se as empresas já existem para evitar duplicatas em reinícios da API
            if (enterpriseRepository.count() == 0) {
                System.out.println("🚀 Initializing data for load testing...");

                // --- Geração de Empresas ---
                int numEnterprises = 50; // Aumentei para 500 para ter mais base
                List<EnterpriseEntity> enterprises = new ArrayList<>();
                System.out.println("   - Creating " + numEnterprises + " Enterprises...");
                for (int i = 0; i < numEnterprises; i++) {
                    String cnpj = faker.number().digits(14);
                    EnterpriseEntity enterprise = new EnterpriseEntity(faker.company().name() + " Ltda. " + i, cnpj, LocalDateTime.now());
                    enterprises.add(enterprise);
                }
                enterpriseRepository.saveAll(enterprises);
                System.out.println("   ✅ Enterprises created!");

                // --- Geração de Usuários ---
                int usersPerEnterprise = 20;
                List<UserEntity> users = new ArrayList<>();
                String defaultPassword = "senha123";
                String encodedDefaultPassword = passwordEncoder.encode(defaultPassword);

                for (int i = 0; i < numEnterprises; i++) {
                    EnterpriseEntity enterprise = enterprises.get(i);
                    for (int j = 0; j < usersPerEnterprise; j++) {
                        String userName = "user" + i + "_" + j;
                        String plainCpf;
                        String encryptedCpf;
                        int attempts = 0;

                        do {
                            plainCpf = faker.number().digits(11);
                            attempts++;
                            if (attempts > 100 && generatedCpfs.contains(plainCpf)) { // Aumentei tentativas
                                System.err.println("      ❌ Could not generate unique and valid CPF after many attempts. Skipping user.");
                                plainCpf = null;
                                break;
                            }
                        } while (generatedCpfs.contains(plainCpf) || !CpfUtil.isValidCpf(plainCpf)); // <--- Valida CPF aqui!

                        if (plainCpf == null) continue;

                        try {
                            encryptedCpf = encryptionUtil.encrypt(plainCpf);
                        } catch (Exception e) {
                            System.err.println("      ❌ Error encrypting CPF for user " + userName + ": " + e.getMessage());
                            continue;
                        }

                        generatedCpfs.add(plainCpf);

                        UserEntity user = new UserEntity(userName, encryptedCpf, encodedDefaultPassword, enterprise);
                        users.add(user);
                    }
                }
                userRepository.saveAll(users);
                System.out.println("   ✅ Users created!");

                // --- Geração de Benefícios ---
                int benefitsPerEnterprise = 10; // 10 benefícios por empresa
                List<BenefitEntity> benefits = new ArrayList<>();
                System.out.println("   - Creating " + (numEnterprises * benefitsPerEnterprise) + " Benefits...");
                BenefitCategory[] categories = BenefitCategory.values();

                for (int i = 0; i < numEnterprises; i++) {
                    EnterpriseEntity supplierEnterprise = enterprises.get(i);
                    for (int j = 0; j < benefitsPerEnterprise; j++) {
                        BenefitCategory category = categories[faker.random().nextInt(categories.length)];
                        BenefitEntity benefit = new BenefitEntity(faker.commerce().productName() + " (Cat: " + category + ")", supplierEnterprise, category);
                        benefits.add(benefit);
                    }
                }
                benefitRepository.saveAll(benefits);
                System.out.println("   ✅ Benefits created!");

                // --- Geração de Parcerias ---
                // Para evitar duplicatas e ter parcerias relevantes
                int numPartnerships = 100; // Tentar criar 1000 parcerias
                List<PartnershipEntity> partnerships = new ArrayList<>();
                Set<String> existingPartnerships = new HashSet<>(); // Para evitar duplicatas

//                System.out.println("   - Creating " + numPartnerships + " Partnerships...");

                for (int i = 0; i < numPartnerships; i++) {
                    EnterpriseEntity consumer = enterprises.get(faker.random().nextInt(enterprises.size()));
                    EnterpriseEntity supplier = enterprises.get(faker.random().nextInt(enterprises.size()));

                    // Garante que as empresas são diferentes e a parceria ainda não existe
                    int attempts = 0;
                    while (consumer.equals(supplier) || existingPartnerships.contains(consumer.getId() + "-" + supplier.getId()) || existingPartnerships.contains(supplier.getId() + "-" + consumer.getId())) {
                        supplier = enterprises.get(faker.random().nextInt(enterprises.size()));
                        consumer = enterprises.get(faker.random().nextInt(enterprises.size()));
                        attempts++;
                        if (attempts > numEnterprises * 2) { // Evita loop infinito se não houver mais combinações
                            System.out.println("      ⚠️ Could not create more unique partnerships after " + i + " attempts.");
                            break;
                        }
                    }

                    if (!consumer.equals(supplier) && !existingPartnerships.contains(consumer.getId() + "-" + supplier.getId()) && !existingPartnerships.contains(supplier.getId() + "-" + consumer.getId())) {
                        PartnershipEntity partnership = new PartnershipEntity(consumer, supplier);
                        partnerships.add(partnership);
                        existingPartnerships.add(consumer.getId() + "-" + supplier.getId());
                    }
                }
                partnershipRepository.saveAll(partnerships);
                System.out.println("   ✅ Partnerships created!");

                System.out.println("✅ Data initialization complete!");

            } else {
                System.out.println("ℹ️ Data already exist. Skipping initialization.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error during data initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
