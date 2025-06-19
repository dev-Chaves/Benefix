package com.hackaton.desafio.config;

import com.hackaton.desafio.entity.UserEntity;
import com.hackaton.desafio.repository.UserRepository;
import com.hackaton.desafio.util.EncryptionUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;

    public CustomUserDetailsService(UserRepository userRepository, EncryptionUtil encryptionUtil) {
        this.userRepository = userRepository;
        this.encryptionUtil = encryptionUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {

        if (cpf == null || cpf.isEmpty()) {
            throw new UsernameNotFoundException("CPF cannot be null or empty");
        }

        try {
            String encryptedCpf = encryptionUtil.encrypt(cpf);

            return userRepository.findByCpf(encryptedCpf)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with CPF: " + cpf));

        } catch (Exception e) {
            throw new RuntimeException("Error processing user authentication", e);
        }
    }
}