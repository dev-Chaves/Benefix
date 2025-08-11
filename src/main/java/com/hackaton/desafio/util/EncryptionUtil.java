package com.hackaton.desafio.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class EncryptionUtil {

    @Value("${ENCRYPTION_KEY:1234567890123456}")
    private String encryptionKey;

    private Cipher cipher;
    private SecretKeySpec secretKeySpec;

    @PostConstruct
    public void init() throws Exception {
        if (encryptionKey == null || encryptionKey.trim().isEmpty()) {
            throw new IllegalStateException("Chave de criptografia não configurada");
        }

        if (encryptionKey.length() != 16 && encryptionKey.length() != 24 && encryptionKey.length() != 32) {
            throw new IllegalArgumentException("Chave deve ter 16, 24, ou 32 caracteres");
        }

        this.secretKeySpec = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

        public String encrypt (String plainText) throws  Exception{
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        }

        public String decrypt (String encryptedText)throws Exception{
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decoded = Base64.getDecoder().decode(encryptedText);
        return new String (cipher.doFinal(decoded),StandardCharsets.UTF_8);
    }


}