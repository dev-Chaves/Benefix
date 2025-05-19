package com.hackaton.desafio.util;

import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.repository.EnterpriseRepository;
import jakarta.persistence.Column;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EnterpriseRepository enterpriseRepository;

    public DataInitializer(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    @Override
    public void run(String... args) {
        try {
            if (enterpriseRepository.count() == 0) {
                EnterpriseEntity enterprise = new EnterpriseEntity();
                enterprise.setEnterprise("Empresa Padrão");
                enterprise.setCnpj("12345678000100");
                enterprise.setCreatedAt(LocalDateTime.now());

                enterpriseRepository.save(enterprise);

                System.out.println("✅ Enterprise create with sucess");
            } else {
                System.out.println("ℹ️ Enterprise already exist.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error on creation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
