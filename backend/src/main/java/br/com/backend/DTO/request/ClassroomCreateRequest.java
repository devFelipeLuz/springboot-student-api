package br.com.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ClassroomCreateRequest(
        @NotBlank(message = "Classroom is required")
        String name,

        @NotNull @NotBlank(message = "SchoolYearID is required")
        UUID schoolYearId
) {}
