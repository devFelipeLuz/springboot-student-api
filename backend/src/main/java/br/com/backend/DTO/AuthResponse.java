package br.com.backend.DTO;

public record AuthResponse (
        String accessToken,
        String refreshToken
){}
