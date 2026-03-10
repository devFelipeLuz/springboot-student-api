package br.com.backend.DTO.grade;

import jakarta.validation.constraints.NotNull;

public record GradeUpdateDTO(
        @NotNull Double grade
) {}
