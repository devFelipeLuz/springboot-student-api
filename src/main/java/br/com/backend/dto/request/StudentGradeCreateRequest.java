package br.com.backend.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StudentGradeCreateRequest(
        @NotNull(message = "AssessmentID is required")
        UUID assessmentId,

        @NotNull(message = "EnrollmentID is required")
        UUID enrollmentId,

        @NotNull(message = "Max score is required")
        Double maxScore
) {}
