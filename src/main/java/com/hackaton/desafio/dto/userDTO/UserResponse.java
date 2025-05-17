package com.hackaton.desafio.dto.userDTO;

import com.hackaton.desafio.entity.Role.Role;

public record UserResponse(
        String name,
        Role role,
        String empresa
) {
}
