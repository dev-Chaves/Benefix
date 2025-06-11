package com.hackaton.desafio.util.validation;

import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    public void validateUserInput(String name, String password, String token) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        if(token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or blank");
        }
    }

}
