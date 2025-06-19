package com.hackaton.desafio.util.validation.validators;

import com.hackaton.desafio.dto.authDTO.LoginRequestV2;
import com.hackaton.desafio.util.validation.Validator;

public class LoginValidatorCpf implements Validator<LoginRequestV2> {

    @Override
    public void validate(LoginRequestV2 input) {
        if (input.cpf() == null || input.cpf().isBlank()) {
            throw new IllegalArgumentException("Invalid Credentials");
        }
        if (input.password() == null || input.password().isBlank()) {
            throw new IllegalArgumentException("Invalid Credentials");
        }
    }
}
