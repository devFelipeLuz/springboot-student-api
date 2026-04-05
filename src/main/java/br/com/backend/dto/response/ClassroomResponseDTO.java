package br.com.backend.dto.response;

import java.util.UUID;

public record ClassroomResponseDTO(
        UUID id,
        String name
) {}
