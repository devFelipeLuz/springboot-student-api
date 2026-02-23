package br.com.backend.DTO;

public record LoginRequest(
        String username,
        String password
) {}