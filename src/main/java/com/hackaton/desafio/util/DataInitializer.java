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

    // Configurações reduzidas para evitar timeout
    private static final int NUM_ENTERPRISES = 50;        // Reduzido de 500 para 50
    private static final int USERS_PER_ENTERPRISE = 5;    // Reduzido de 50 para 5
    private static final int BENEFITS_PER_ENTERPRISE = 3; // Reduzido de 10 para 3
    private static final int NUM_PARTNERSHIPS = 100;      // Reduzido de 1000 para 100
    private static final int BATCH_SIZE = 100;            // Tamanho do lote para salvar

    public DataInitializer(EnterpriseRepository enterpriseRepository,
                           UserRepository userRepository,
                           BenefitRepository benefitRepository,
                           PartnershipRepository partnershipRepository,
                           PasswordEncoder passwordEncoder,
                           EncryptionUtil encryptionUtil) {
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
            // Verifica se os dados já existem
            if (enterpriseRepository.count() == 0) {
                System.out.println("🚀 Initializing optimized data set...");

                long startTime = System.currentTimeMillis();

                // --- Criação de Empresas ---
                List<EnterpriseEntity> enterprises = createEnterprises();

                // --- Criação de Usuários em lotes ---
                createUsersInBatches(enterprises);

                // --- Criação de Benefícios em lotes ---
                createBenefitsInBatches(enterprises);

                // --- Criação de Parcerias ---
                createPartnerships(enterprises);

                long endTime = System.currentTimeMillis();
                long duration = (endTime - startTime) / 1000;

                System.out.println("✅ Data initialization complete in " + duration + " seconds!");
                System.out.println("📊 Summary:");
                System.out.println("   - Enterprises: " + NUM_ENTERPRISES);
                System.out.println("   - Users: " + (NUM_ENTERPRISES * USERS_PER_ENTERPRISE));
                System.out.println("   - Benefits: " + (NUM_ENTERPRISES * BENEFITS_PER_ENTERPRISE));
                System.out.println("   - Partnerships: " + NUM_PARTNERSHIPS);

            } else {
                System.out.println("ℹ️ Data already exists. Skipping initialization.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error during data initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<EnterpriseEntity> createEnterprises() {
        System.out.println("   - Creating " + NUM_ENTERPRISES + " enterprises...");
        List<EnterpriseEntity> enterprises = new ArrayList<>();

        for (int i = 0; i < NUM_ENTERPRISES; i++) {
            String cnpj = generateCnpj(i);
            EnterpriseEntity enterprise = new EnterpriseEntity(
                    faker.company().name() + " Ltda " + (i + 1),
                    cnpj,
                    LocalDateTime.now()
            );
            enterprises.add(enterprise);
        }

        enterpriseRepository.saveAll(enterprises);
        System.out.println("   ✅ Enterprises created!");
        return enterprises;
    }

    private void createUsersInBatches(List<EnterpriseEntity> enterprises) {
        System.out.println("   - Creating users in batches...");

        String encodedPassword = passwordEncoder.encode("senha123");
        List<UserEntity> userBatch = new ArrayList<>();
        int totalUsers = 0;
        int skippedUsers = 0;

        for (int i = 0; i < enterprises.size(); i++) {
            EnterpriseEntity enterprise = enterprises.get(i);

            for (int j = 0; j < USERS_PER_ENTERPRISE; j++) {
                try {
                    UserEntity user = createUser(enterprise, i, j, encodedPassword);
                    if (user != null) {
                        userBatch.add(user);
                        totalUsers++;

                        // Salva em lotes para evitar sobrecarga
                        if (userBatch.size() >= BATCH_SIZE) {
                            userRepository.saveAll(userBatch);
                            userBatch.clear();
                            System.out.println("     📦 Saved batch of users. Total: " + totalUsers);
                        }
                    } else {
                        skippedUsers++;
                        System.err.println("     ⚠️ Skipped user creation for enterprise " + i + ", user " + j);
                    }
                } catch (Exception e) {
                    skippedUsers++;
                    System.err.println("     ❌ Error creating user for enterprise " + i + ", user " + j + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        // Salva o último lote
        if (!userBatch.isEmpty()) {
            try {
                userRepository.saveAll(userBatch);
                System.out.println("     📦 Saved final batch of users.");
            } catch (Exception e) {
                System.err.println("     ❌ Error saving final batch: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("   ✅ " + totalUsers + " users created successfully!");
        if (skippedUsers > 0) {
            System.out.println("   ⚠️ " + skippedUsers + " users were skipped due to errors.");
        }
    }

    private UserEntity createUser(EnterpriseEntity enterprise, int enterpriseIndex, int userIndex, String encodedPassword) {
        String userName = "user" + enterpriseIndex + "_" + userIndex;

        // Gera CPF sequencial - muito mais simples e confiável
        String plainCpf = generateSequentialCpf(enterpriseIndex, userIndex);

        try {
            String encryptedCpf = encryptionUtil.encrypt(plainCpf);
            // Ordem correta dos parâmetros no construtor
            return new UserEntity(userName, encodedPassword, encryptedCpf, enterprise);
        } catch (Exception e) {
            System.err.println("     ❌ Error encrypting CPF for " + userName + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void createBenefitsInBatches(List<EnterpriseEntity> enterprises) {
        System.out.println("   - Creating benefits in batches...");

        BenefitCategory[] categories = BenefitCategory.values();
        List<BenefitEntity> benefitBatch = new ArrayList<>();
        int totalBenefits = 0;

        for (int i = 0; i < enterprises.size(); i++) {
            EnterpriseEntity enterprise = enterprises.get(i);

            for (int j = 0; j < BENEFITS_PER_ENTERPRISE; j++) {
                BenefitCategory category = categories[faker.random().nextInt(categories.length)];
                BenefitEntity benefit = new BenefitEntity(
                        faker.commerce().productName() + " - " + category.name(),
                        enterprise,
                        category
                );
                benefitBatch.add(benefit);
                totalBenefits++;

                // Salva em lotes
                if (benefitBatch.size() >= BATCH_SIZE) {
                    benefitRepository.saveAll(benefitBatch);
                    benefitBatch.clear();
                    System.out.println("     📦 Saved batch of benefits. Total: " + totalBenefits);
                }
            }
        }

        // Salva o último lote
        if (!benefitBatch.isEmpty()) {
            benefitRepository.saveAll(benefitBatch);
        }

        System.out.println("   ✅ " + totalBenefits + " benefits created!");
    }

    private void createPartnerships(List<EnterpriseEntity> enterprises) {
        System.out.println("   - Creating partnerships...");

        List<PartnershipEntity> partnerships = new ArrayList<>();
        Set<String> existingPartnerships = new HashSet<>();

        int created = 0;
        int attempts = 0;
        int maxAttempts = NUM_PARTNERSHIPS * 3; // Limite de tentativas

        while (created < NUM_PARTNERSHIPS && attempts < maxAttempts) {
            attempts++;

            EnterpriseEntity consumer = enterprises.get(faker.random().nextInt(enterprises.size()));
            EnterpriseEntity supplier = enterprises.get(faker.random().nextInt(enterprises.size()));

            // Verifica se é uma parceria válida
            if (!consumer.equals(supplier)) {
                String partnershipKey = consumer.getId() + "-" + supplier.getId();
                String reverseKey = supplier.getId() + "-" + consumer.getId();

                if (!existingPartnerships.contains(partnershipKey) &&
                        !existingPartnerships.contains(reverseKey)) {

                    PartnershipEntity partnership = new PartnershipEntity(consumer, supplier);
                    partnerships.add(partnership);
                    existingPartnerships.add(partnershipKey);
                    created++;
                }
            }
        }

        partnershipRepository.saveAll(partnerships);
        System.out.println("   ✅ " + created + " partnerships created!");
    }

    private String generateCnpj(int index) {
        // CNPJ sequencial: 1000000000000 + index (13 dígitos + 1 dígito do index)
        return String.format("%013d", 1000000000000L + index);
    }

    private String generateSequentialCpf(int enterpriseIndex, int userIndex) {
        // CPF sequencial: 10000000000 + (enterpriseIndex * 100 + userIndex)
        // Garante que cada usuário tenha um CPF único baseado na empresa e posição
        long cpfNumber = 10000000000L + (enterpriseIndex * 1000L + userIndex);
        String cpf = String.format("%011d", cpfNumber);

        // Adiciona ao conjunto para tracking (mesmo sendo sequencial)
        generatedCpfs.add(cpf);
        return cpf;
    }
}