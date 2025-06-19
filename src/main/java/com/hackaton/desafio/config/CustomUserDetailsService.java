package com.hackaton.desafio.config;

import com.hackaton.desafio.entity.UserEntity;
import com.hackaton.desafio.repository.UserRepository;
import com.hackaton.desafio.util.EncryptionUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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
            throw new UsernameNotFoundException("Username cannot be null or empty");
        }

        try {
            String encrytedCpf = encryptionUtil.encrypt(cpf);
            return userRepository.findByCpf(cpf).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
