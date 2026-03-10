package br.com.backend.DTO.grade;

import br.com.backend.domain.enums.AssessmentType;

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
