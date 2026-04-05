package br.com.backend.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TeachingAssignmentRequest(
        @NotNull UUID professorId,
        @NotNull UUID subjectId,
        @NotNull UUID classroomId
) {
}
