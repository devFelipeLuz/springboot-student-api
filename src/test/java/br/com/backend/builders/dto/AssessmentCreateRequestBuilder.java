package br.com.backend.builders.dto;

import br.com.backend.dto.request.AssessmentCreateRequest;
import br.com.backend.entity.enums.AssessmentType;

import java.util.UUID;

public class AssessmentCreateRequestBuilder {

    private String title = "Prova de História";
    private AssessmentType type = AssessmentType.PROVA;
    private UUID assignmentId = UUID.randomUUID();

    public static AssessmentCreateRequestBuilder builder() {
        return new AssessmentCreateRequestBuilder();
    }

    public AssessmentCreateRequestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public AssessmentCreateRequestBuilder withType(AssessmentType type) {
        this.type = type;
        return this;
    }

    public AssessmentCreateRequestBuilder withAssignmentId(UUID assignmentId) {
        this.assignmentId = assignmentId;
        return this;
    }

    public AssessmentCreateRequest build() {
        return new AssessmentCreateRequest(title, type, assignmentId);
    }
}
