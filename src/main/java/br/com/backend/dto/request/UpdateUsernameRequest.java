package br.com.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UpdateUsernameRequest(

        @Email
        @NotNull
        String email
) {
}
