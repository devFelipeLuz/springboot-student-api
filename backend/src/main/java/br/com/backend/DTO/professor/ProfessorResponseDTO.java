package br.com.backend.DTO.professor;

import java.util.UUID;

public record ProfessorResponseDTO(
        UUID id,
        String name,
        String username
) {}
