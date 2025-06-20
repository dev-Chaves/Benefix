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
    private final PasswordEncoder passwordEncoder;
    private final EncryptionUtil encryptionUtil;

    public DataInitializer(EnterpriseRepository enterpriseRepository,
                           UserRepository userRepository,
                           BenefitRepository benefitRepository,
                           PartnershipRepository partnershipRepository,
                           PasswordEncoder passwordEncoder, EncryptionUtil encryptionUtil) {
        this.enterpriseRepository = enterpriseRepository;
        this.userRepository = userRepository;
        this.benefitRepository = benefitRepository;
        this.partnershipRepository = partnershipRepository;
        this.passwordEncoder = passwordEncoder;
        this.encryptionUtil = encryptionUtil;
    }

    @Override
    @Transactional
    public void run(String... args) {
        try {
            if (enterpriseRepository.count() == 0) {
                System.out.println("🚀 Initializing data for load testing...");

                // --- Geração de Empresas com CNPJs sequenciais ---
                int numEnterprises = 20; // Reduzido para teste
                List<EnterpriseEntity> enterprises = new ArrayList<>();
                System.out.println("   - Creating " + numEnterprises + " Enterprises...");

                for (int i = 1; i <= numEnterprises; i++) {
                    String cnpj = generateSequentialCnpj(i);
                    String enterpriseName = "Empresa " + i + " Ltda";
                    EnterpriseEntity enterprise = new EnterpriseEntity(enterpriseName, cnpj, LocalDateTime.now());
                    enterprises.add(enterprise);
                }
                enterpriseRepository.saveAll(enterprises);
                System.out.println("   ✅ " + enterprises.size() + " Enterprises created!");

                // --- Geração de Usuários com CPFs sequenciais ---
                int usersPerEnterprise = 5; // Reduzido para teste
                List<UserEntity> users = new ArrayList<>();
                String defaultPassword = passwordEncoder.encode("senha123");
                int userCounter = 1;

                System.out.println("   - Creating users...");

                for (EnterpriseEntity enterprise : enterprises) {
                    for (int j = 1; j <= usersPerEnterprise; j++) {
                        try {
                            String userName = "User " + userCounter;
                            String cpf = generateSequentialCpf(userCounter);

//                            String encryptedCPF = encryptionUtil.encrypt(cpf);

                            // Usando construtor correto: name, password, cpf, enterprise
                            UserEntity user = new UserEntity(userName, defaultPassword, cpf, enterprise);
                            users.add(user);

                            userCounter++;

                            if (userCounter % 10 == 0) {
                                System.out.println("     - Created " + userCounter + " users so far...");
                            }

                        } catch (Exception e) {
                            System.err.println("      ❌ Error creating user " + userCounter + ": " + e.getMessage());
                        }
                    }
                }

                if (!users.isEmpty()) {
                    userRepository.saveAll(users);
                    System.out.println("   ✅ " + users.size() + " Users created!");
                } else {
                    System.out.println("   ❌ No users were created!");
                }

                // --- Geração de Benefícios ---
                int benefitsPerEnterprise = 5; // Reduzido para teste
                List<BenefitEntity> benefits = new ArrayList<>();
                System.out.println("   - Creating benefits...");
                BenefitCategory[] categories = BenefitCategory.values();

                for (int i = 0; i < enterprises.size(); i++) {
                    EnterpriseEntity supplierEnterprise = enterprises.get(i);
                    for (int j = 1; j <= benefitsPerEnterprise; j++) {
                        BenefitCategory category = categories[(i + j) % categories.length];
                        String benefitName = "Benefício " + (i + 1) + "." + j + " - " + category;
                        BenefitEntity benefit = new BenefitEntity(benefitName, supplierEnterprise, category);
                        benefits.add(benefit);
                    }
                }
                benefitRepository.saveAll(benefits);
                System.out.println("   ✅ " + benefits.size() + " Benefits created!");

                // --- Geração de Parcerias ---
                int numPartnerships = Math.min(20, (enterprises.size() * (enterprises.size() - 1)) / 4);
                List<PartnershipEntity> partnerships = new ArrayList<>();
                Set<String> existingPartnerships = new HashSet<>();

                System.out.println("   - Creating partnerships...");

                for (int i = 0; i < numPartnerships; i++) {
                    int consumerIndex = i % enterprises.size();
                    int supplierIndex = (i + 1) % enterprises.size();

                    if (consumerIndex != supplierIndex) {
                        EnterpriseEntity consumer = enterprises.get(consumerIndex);
                        EnterpriseEntity supplier = enterprises.get(supplierIndex);

                        String partnershipKey = consumer.getId() + "-" + supplier.getId();
                        String reverseKey = supplier.getId() + "-" + consumer.getId();

                        if (!existingPartnerships.contains(partnershipKey) &&
                                !existingPartnerships.contains(reverseKey)) {

                            PartnershipEntity partnership = new PartnershipEntity(consumer, supplier);
                            partnerships.add(partnership);
                            existingPartnerships.add(partnershipKey);
                        }
                    }
                }

                if (!partnerships.isEmpty()) {
                    partnershipRepository.saveAll(partnerships);
                    System.out.println("   ✅ " + partnerships.size() + " Partnerships created!");
                }

                System.out.println("✅ Data initialization complete!");
                System.out.println("📊 Summary:");
                System.out.println("   - Enterprises: " + enterprises.size());
                System.out.println("   - Users: " + users.size());
                System.out.println("   - Benefits: " + benefits.size());
                System.out.println("   - Partnerships: " + partnerships.size());

            } else {
                System.out.println("ℹ️ Data already exists. Skipping initialization.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error during data initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gera um CPF sequencial válido baseado no número
     */
    private String generateSequentialCpf(int number) {
        // Base do CPF com 9 dígitos, preenchido com zeros à esquerda
        String base = String.format("%09d", number % 999999999);

        // Calcula os dígitos verificadores
        int firstDigit = calculateCpfDigit(base, 10);
        int secondDigit = calculateCpfDigit(base + firstDigit, 11);

        return base + firstDigit + secondDigit;
    }

    /**
     * Gera um CNPJ sequencial válido baseado no número
     */
    private String generateSequentialCnpj(int number) {
        // Base do CNPJ com 12 dígitos, preenchido com zeros à esquerda
        String base = String.format("%012d", number % 999999999999L);

        // Calcula os dígitos verificadores do CNPJ
        int firstDigit = calculateCnpjDigit(base, new int[]{5,4,3,2,9,8,7,6,5,4,3,2});
        int secondDigit = calculateCnpjDigit(base + firstDigit, new int[]{6,5,4,3,2,9,8,7,6,5,4,3,2});

        return base + firstDigit + secondDigit;
    }

    /**
     * Calcula dígito verificador do CPF
     */
    private int calculateCpfDigit(String cpf, int weight) {
        int sum = 0;
        for (int i = 0; i < cpf.length(); i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * weight--;
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    /**
     * Calcula dígito verificador do CNPJ
     */
    private int calculateCnpjDigit(String cnpj, int[] weights) {
        int sum = 0;
        for (int i = 0; i < cnpj.length(); i++) {
            sum += Character.getNumericValue(cnpj.charAt(i)) * weights[i];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}