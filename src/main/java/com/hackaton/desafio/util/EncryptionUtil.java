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
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class EncryptUtil {

    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    @Value("${ENCRYPTION_KEY}") // A chave de criptografia será lida de application.yaml ou .env
    private String encryptionKey;

    private SecretKeySpec secretKeySpec;

    public void init(){
        if (encryptionKey.length() != 16 && encryptionKey.length() != 24 && encryptionKey.length() != 32){
            throw new IllegalArgumentException("Encryption key deve ser 16, 24, 32");
        }
        this.secretKeySpec = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }

    public String encrypt(String textToEncrypt) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        if (secretKeySpec == null){
            init();
        }

        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);

        SecureRandom secureRandom = new SecureRandom();

        byte[] iv = new byte[cipher.getBlockSize()];

        secureRandom.nextBytes(iv);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        byte[] encryptedBytes = cipher.doFinal(textToEncrypt.getBytes(StandardCharsets.UTF_8));


        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combined);

    }

    public String decrypt(String encryptedText) throws Exception {
        if (secretKeySpec == null) {
            init(); // Garante que a chave foi inicializada
        }

        byte[] decodedCombined = Base64.getDecoder().decode(encryptedText);

        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        byte[] iv = new byte[cipher.getBlockSize()];
        System.arraycopy(decodedCombined, 0, iv, 0, iv.length);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        byte[] encryptedBytes = new byte[decodedCombined.length - iv.length];
        System.arraycopy(decodedCombined, iv.length, encryptedBytes, 0, encryptedBytes.length);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
