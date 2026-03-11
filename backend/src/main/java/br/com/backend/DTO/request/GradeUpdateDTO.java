package br.com.backend.dto.request;

import jakarta.validation.constraints.NotNull;

public record GradeUpdateDTO(
        @NotNull(message = "new grade is required")
        Double grade
) {}
