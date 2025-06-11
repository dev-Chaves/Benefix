package com.hackaton.desafio.util.validation.validators;

import com.hackaton.desafio.dto.authDTO.RegisterDTO;
import com.hackaton.desafio.repository.UserRepository;
import com.hackaton.desafio.util.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class RegisterValidator implements Validator<RegisterDTO> {

    @Override
    public void validate(RegisterDTO input) {
        if(input.name() == null || input.name().isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if(input.password() == null || input.password().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        if(input.token() == null || input.token().isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or blank");
        }
    }


}
