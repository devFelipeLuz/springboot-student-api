package br.com.backend.dto.response;

import java.util.UUID;

public record ProfessorResponseDTO(
        UUID id,
        String name,
        String username
) {}
