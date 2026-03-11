package br.com.backend.dto.response;

public record AuthResponse (
        String accessToken,
        String refreshToken
){}
