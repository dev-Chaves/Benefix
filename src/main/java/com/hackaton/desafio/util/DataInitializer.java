package com.hackaton.desafio.util;

import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.entity.Enum.BenefitCategory;
import com.hackaton.desafio.entity.UserEntity;
import com.hackaton.desafio.repository.BenefitRepository;
import com.hackaton.desafio.repository.EnterpriseRepository;
import com.hackaton.desafio.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EnterpriseRepository enterpriseRepository;
    private final UserRepository userRepository;
    private final BenefitRepository benefitRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionUtil encryptionUtil;

    public DataInitializer(EnterpriseRepository enterpriseRepository,
                           UserRepository userRepository,
                           BenefitRepository benefitRepository,
                           PasswordEncoder passwordEncoder,
                           EncryptionUtil encryptionUtil) {
        this.enterpriseRepository = enterpriseRepository;
        this.userRepository = userRepository;
        this.benefitRepository = benefitRepository;
        this.passwordEncoder = passwordEncoder;
        this.encryptionUtil = encryptionUtil;
    }

    @Override
    public void run(String... args) {
        // Só executa se não existirem dados
        if (enterpriseRepository.count() > 0) {
            System.out.println("📊 Dados já existem. Pulando inicialização.");
            return;
        }

        System.out.println("🚀 Inicializando dados ultra simplificados...");

        try {
            // Criar algumas empresas fixas
            List<EnterpriseEntity> empresas = criarEmpresas();

            // Criar alguns usuários para cada empresa
            criarUsuarios(empresas);

            // Criar alguns benefícios
            criarBeneficios(empresas);

            System.out.println("✅ Dados criados com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ Erro na inicialização: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<EnterpriseEntity> criarEmpresas() {
        System.out.println("   - Criando empresas...");

        List<EnterpriseEntity> empresas = new ArrayList<>();

        // Empresas fixas - super simples
        String[] nomesEmpresas = {
                "Tech Solutions Ltda",
                "Digital Corp",
                "Innovation Hub",
                "StartUp Brasil",
                "Future Tech"
        };

        for (int i = 0; i < nomesEmpresas.length; i++) {
            String cnpj = String.format("1234567800%02d", i + 1); // CNPJ simples
            EnterpriseEntity empresa = new EnterpriseEntity(
                    nomesEmpresas[i],
                    cnpj,
                    LocalDateTime.now()
            );
            empresas.add(empresa);
        }

        enterpriseRepository.saveAll(empresas);
        System.out.println("   ✅ " + empresas.size() + " empresas criadas!");

        return empresas;
    }

    private void criarUsuarios(List<EnterpriseEntity> empresas) {
        System.out.println("   - Criando usuários...");

        List<UserEntity> usuarios = new ArrayList<>();
        String senhaEncriptada = passwordEncoder.encode("123456"); // Senha padrão

        // CPFs simples e válidos para teste
        String[] cpfsBase = {
                "12345678901",
                "98765432100",
                "11122233344",
                "55566677788",
                "99988877766"
        };

        int cpfIndex = 0;

        for (EnterpriseEntity empresa : empresas) {
            // 2 usuários por empresa
            for (int i = 1; i <= 2; i++) {
                try {
                    String cpfSimples = cpfsBase[cpfIndex % cpfsBase.length] + i;
                    // Ajustar para ter exatamente 11 dígitos
                    if (cpfSimples.length() > 11) {
                        cpfSimples = cpfSimples.substring(0, 11);
                    }

                    String cpfEncriptado = encryptionUtil.encrypt(cpfSimples);

                    UserEntity usuario = new UserEntity(
                            "usuario" + (cpfIndex + 1) + "_" + i,
                            senhaEncriptada,
                            cpfEncriptado,
                            empresa
                    );

                    usuarios.add(usuario);
                    cpfIndex++;

                } catch (Exception e) {
                    System.err.println("     ⚠️ Erro ao criar usuário: " + e.getMessage());
                }
            }
        }

        if (!usuarios.isEmpty()) {
            userRepository.saveAll(usuarios);
            System.out.println("   ✅ " + usuarios.size() + " usuários criados!");
        }
    }

    private void criarBeneficios(List<EnterpriseEntity> empresas) {
        System.out.println("   - Criando benefícios...");

        List<BenefitEntity> beneficios = new ArrayList<>();

        String[] nomesBeneficios = {
                "Vale Alimentação",
                "Plano de Saúde",
                "Vale Transporte",
                "Seguro de Vida",
                "Auxílio Educação"
        };

        BenefitCategory[] categorias = BenefitCategory.values();

        for (EnterpriseEntity empresa : empresas) {
            // 2 benefícios por empresa
            for (int i = 0; i < 2 && i < nomesBeneficios.length; i++) {
                BenefitEntity beneficio = new BenefitEntity(
                        nomesBeneficios[i],
                        empresa,
                        categorias[i % categorias.length]
                );
                beneficios.add(beneficio);
            }
        }

        if (!beneficios.isEmpty()) {
            benefitRepository.saveAll(beneficios);
            System.out.println("   ✅ " + beneficios.size() + " benefícios criados!");
        }
    }
}