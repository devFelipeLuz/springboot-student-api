package br.com.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ForgotPasswordRequest(
        @NotNull @NotBlank(message = "Email is required")
        String email
) {}
