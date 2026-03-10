package br.com.backend.DTO.professor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProfessorRequestDTO(
        @NotNull @NotBlank String name,
        @NotNull UUID userId
        ) {}
