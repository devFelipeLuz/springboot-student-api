package br.com.backend.dto.request;

import br.com.backend.entity.enums.AssessmentType;

import java.util.UUID;

public record AssessmentUpdateRequest(
        String title,
        AssessmentType type,
        UUID teachingAssignmentId
) {
}
