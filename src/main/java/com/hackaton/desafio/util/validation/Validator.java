package com.hackaton.desafio.util.validation;

public interface Validator<T> {
    void validate(T input);
}
