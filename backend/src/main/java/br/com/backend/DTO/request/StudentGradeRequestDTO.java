package br.com.backend.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StudentGradeRequestDTO(
        @NotNull(message = "AssessmentID is required")
        UUID assessmentId,

        @NotNull(message = "EnrollmentID is required")
        UUID enrollmentId
) {}
