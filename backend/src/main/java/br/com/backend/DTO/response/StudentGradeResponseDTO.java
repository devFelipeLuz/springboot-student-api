package br.com.backend.dto.response;

import br.com.backend.entity.enums.AssessmentType;

import java.time.Instant;
import java.util.UUID;

public record StudentGradeResponseDTO(
        UUID id,
        String studentName,
        Double grade,
        String title,
        AssessmentType type,
        Instant date,
        String professorName,
        String subject
) {}
