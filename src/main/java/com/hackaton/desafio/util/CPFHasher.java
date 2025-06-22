package com.hackaton.desafio.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class CPFHasher {

    @Value("${ENCRYPTION_KEY:1234567890123456}")
    private String key;

    public String hashCPF(String cpf){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = key + cpf.trim();

            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for(byte b : hash){
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }catch (Exception e){
            throw new RuntimeException("Failed to encrypt the data");
        }
    }

}
