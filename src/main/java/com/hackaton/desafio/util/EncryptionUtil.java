package com.hackaton.desafio.util;

import org.springframework.stereotype.Component;
import java.util.Base64;

@Component
public class EncryptionUtil {

    // Chave fixa super simples - apenas para demonstração
    private static final String SIMPLE_KEY = "HACKATON2024";

    public String encrypt(String plainText) {
        try {
            // Criptografia ultra simples: XOR + Base64
            StringBuilder encrypted = new StringBuilder();

            for (int i = 0; i < plainText.length(); i++) {
                char plainChar = plainText.charAt(i);
                char keyChar = SIMPLE_KEY.charAt(i % SIMPLE_KEY.length());
                char encryptedChar = (char) (plainChar ^ keyChar);
                encrypted.append(encryptedChar);
            }

            // Codifica em Base64 para ter uma string segura
            return Base64.getEncoder().encodeToString(encrypted.toString().getBytes());

        } catch (Exception e) {
            throw new RuntimeException("Erro na criptografia simples", e);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            // Decodifica do Base64
            String xorString = new String(Base64.getDecoder().decode(encryptedText));

            // Aplica XOR novamente para descriptografar
            StringBuilder decrypted = new StringBuilder();

            for (int i = 0; i < xorString.length(); i++) {
                char encryptedChar = xorString.charAt(i);
                char keyChar = SIMPLE_KEY.charAt(i % SIMPLE_KEY.length());
                char decryptedChar = (char) (encryptedChar ^ keyChar);
                decrypted.append(decryptedChar);
            }

            return decrypted.toString();

        } catch (Exception e) {
            throw new RuntimeException("Erro na descriptografia simples", e);
        }
    }
}