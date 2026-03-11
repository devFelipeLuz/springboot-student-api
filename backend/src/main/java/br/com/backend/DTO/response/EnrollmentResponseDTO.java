package br.com.backend.dto.response;

import java.util.UUID;

public record EnrollmentResponseDTO(
        UUID id,
        String studentName,
        String classroomName,
        Integer schoolYearName
) {}
