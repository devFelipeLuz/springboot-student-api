package br.com.backend.dto.request;

import br.com.backend.entity.Classroom;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record StudentRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @Email
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Classroom is required")
        Classroom classroom
) {}
