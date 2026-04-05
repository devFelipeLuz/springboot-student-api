package br.com.backend.dto.request;

import br.com.backend.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
        @NotNull @NotBlank(message = "Username is required")
        String email,

        @NotNull @NotBlank(message = "password is required")
        String password,

        @NotNull
        Role role
) {}
