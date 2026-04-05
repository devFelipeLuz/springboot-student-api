package br.com.backend.dto.request;

public record ProfessorUpdateRequest(
        String name,
        String email,
        String password
) {
}
