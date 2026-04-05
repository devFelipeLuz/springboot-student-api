package br.com.backend.dto.response;

import java.util.UUID;

public record SubjectResponseDTO(
        UUID id,
        String name,
        Boolean active
) {
}
