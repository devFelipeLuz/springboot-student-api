package br.com.backend.dto.response;

import br.com.backend.entity.enums.Role;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        Role role
) {}
