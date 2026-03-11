package br.com.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthRequest(
        @NotNull @NotBlank(message = "username is required")
        String username,

        @NotNull @NotBlank(message = "password is required")
        String password
) {}