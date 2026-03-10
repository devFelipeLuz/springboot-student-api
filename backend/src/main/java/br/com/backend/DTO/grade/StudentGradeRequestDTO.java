package br.com.backend.DTO.grade;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StudentGradeRequestDTO(
        @NotNull UUID assessmentId,
        @NotNull UUID enrollmentId
) {}
