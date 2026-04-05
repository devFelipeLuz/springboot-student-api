package br.com.backend.dto.response;

import java.time.Instant;
import java.util.UUID;

public record SchoolYearResponseDTO(
        UUID id,
        Integer year,
        Instant startDate
) {}
