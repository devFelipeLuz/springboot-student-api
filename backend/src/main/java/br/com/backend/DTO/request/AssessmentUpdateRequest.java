package br.com.backend.dto.request;

import br.com.backend.entity.enums.AssessmentType;

public record AssessmentUpdateRequest(
        String title,
        AssessmentType type
) {
}
