package br.com.backend.dto.response;

import br.com.backend.entity.enums.EnrollmentStatus;

import java.util.UUID;

public record EnrollmentResponseDTO(
        UUID id,
        String studentName,
        Integer schoolYearName,
        String classroomName,
        EnrollmentStatus status
) {}
