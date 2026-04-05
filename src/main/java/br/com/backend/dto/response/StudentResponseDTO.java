package br.com.backend.dto.response;

import java.util.UUID;

public record StudentResponseDTO(
        UUID id,
        String name,
        String email,
        String classroom,
        boolean active
) {}
