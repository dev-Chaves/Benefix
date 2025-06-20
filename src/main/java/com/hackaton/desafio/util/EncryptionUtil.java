package com.hackaton.desafio.util;

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

    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    @Value("${ENCRYPTION_KEY:1234567890123456}") // Valor padrão para desenvolvimento
    private String encryptionKey;

    private SecretKeySpec secretKeySpec;

    public void init(){
        if (encryptionKey.length() != 16 && encryptionKey.length() != 24 && encryptionKey.length() != 32){
            throw new IllegalArgumentException("Encryption key deve ter 16, 24 ou 32 caracteres");
        }
        this.secretKeySpec = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }

    public String encrypt(String textToEncrypt) throws Exception {

        if (secretKeySpec == null){
            init();
        }

        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);

        // Gera IV aleatório
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[cipher.getBlockSize()];
        secureRandom.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Inicializa o cipher para encriptação
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

        // Encripta o texto
        byte[] encryptedBytes = cipher.doFinal(textToEncrypt.getBytes(StandardCharsets.UTF_8));

        // Combina IV + dados encriptados
        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public String decrypt(String encryptedText) throws Exception {
        if (secretKeySpec == null) {
            init();
        }

        byte[] decodedCombined = Base64.getDecoder().decode(encryptedText);

        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);

        // Extrai o IV
        byte[] iv = new byte[cipher.getBlockSize()];
        System.arraycopy(decodedCombined, 0, iv, 0, iv.length);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Extrai os dados encriptados
        byte[] encryptedBytes = new byte[decodedCombined.length - iv.length];
        System.arraycopy(decodedCombined, iv.length, encryptedBytes, 0, encryptedBytes.length);

        // Inicializa o cipher para descriptação
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // Método de conveniência para encriptar sem lançar exceções checked
    public String encryptSafe(String textToEncrypt) {
        try {
            return encrypt(textToEncrypt);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao encriptar texto: " + e.getMessage(), e);
        }
    }

    // Método de conveniência para descriptografar sem lançar exceções checked
    public String decryptSafe(String encryptedText) {
        try {
            return decrypt(encryptedText);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar texto: " + e.getMessage(), e);
        }
    }
}