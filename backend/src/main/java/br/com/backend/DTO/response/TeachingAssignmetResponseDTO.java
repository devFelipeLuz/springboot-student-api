package br.com.backend.dto.response;

import java.util.UUID;

public record TeachingAssignmetResponseDTO(
        UUID id,
        String professorName,
        String subjectName,
        String classroomName
) {
}
