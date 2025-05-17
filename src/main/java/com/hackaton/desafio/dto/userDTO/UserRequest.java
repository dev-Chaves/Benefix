package com.hackaton.desafio.dto.userDTO;

import com.hackaton.desafio.entity.Role.Role;

public record UserRequest(
        String name,
        String password,
        Long enterprise,
        Role role
        ) {
}
