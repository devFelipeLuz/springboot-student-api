package br.com.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePasswordRequest(
        @NotNull
        @NotBlank
        String password
) {
}
