package br.com.backend.dto.request;

import br.com.backend.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequestDTO(
        @NotNull @NotBlank(message = "Username is required")
        String email,

        @NotNull @NotBlank(message = "password is required")
        String password,

        @NotNull @NotBlank
        Role role
) {}
