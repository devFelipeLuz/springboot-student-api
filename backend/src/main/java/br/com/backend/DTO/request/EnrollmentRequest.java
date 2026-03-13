package br.com.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EnrollmentRequest(
        @NotNull @NotBlank(message = "StudentID is required")
        UUID studentId,

        @NotNull @NotBlank(message = "ClassroomID is required")
        UUID classroomId,

        @NotNull @NotBlank(message = "SchoolYearID is required")
        UUID schoolYearId
) {}
