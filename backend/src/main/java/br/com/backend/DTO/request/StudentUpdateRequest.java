package br.com.backend.dto.request;

public record StudentUpdateRequest(
        String name,
        String email,
        String password
) {
}
