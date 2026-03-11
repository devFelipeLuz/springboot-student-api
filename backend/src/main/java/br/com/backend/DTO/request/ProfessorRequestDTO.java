package br.com.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProfessorRequestDTO(
        @NotNull @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "UserID is required")
        UUID userId
        ) {}
