package br.com.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProfessorCreateRequest(
        @NotNull @NotBlank(message = "Name is required")
        String name,

        @Email
        @NotBlank(message = "Email is required")
        String email,

        @NotNull @NotBlank(message = "Password is required")
        String password
        ) {}
