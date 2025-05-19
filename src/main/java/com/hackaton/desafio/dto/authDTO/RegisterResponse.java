package com.hackaton.desafio.dto.authDTO;

import com.hackaton.desafio.entity.Role.Role;

public record AuthResponse(
        String name,
        Role role
) {
}
