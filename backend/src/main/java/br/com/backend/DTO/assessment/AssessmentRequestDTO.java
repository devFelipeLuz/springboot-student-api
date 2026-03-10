package br.com.backend.DTO.assessment;

import br.com.backend.domain.enums.AssessmentType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssessmentRequestDTO (
        @NotNull String title,
        @NotNull AssessmentType type,
        @NotNull UUID teachingAssignmentId
    ) {}
