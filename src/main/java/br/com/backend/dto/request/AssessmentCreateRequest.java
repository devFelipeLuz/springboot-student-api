package br.com.backend.dto.request;

import br.com.backend.entity.enums.AssessmentType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssessmentCreateRequest(
        @NotNull String title,
        @NotNull AssessmentType type,
        @NotNull UUID teachingAssignmentId
    ) {}
