package br.com.backend.dto.request;

public record ResetPasswordRequest(
        String token,
        String newPassword
) {}
