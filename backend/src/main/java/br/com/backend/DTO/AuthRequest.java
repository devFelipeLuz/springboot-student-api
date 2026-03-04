package br.com.backend.DTO;

public record AuthRequest(
        String username,
        String password
) {}