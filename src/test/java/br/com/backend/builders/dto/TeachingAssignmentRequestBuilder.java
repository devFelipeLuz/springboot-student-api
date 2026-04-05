package br.com.backend.builders.dto;

import br.com.backend.dto.request.TeachingAssignmentRequest;

import java.util.UUID;

public class TeachingAssignmentRequestBuilder {

    private UUID professorId = UUID.randomUUID();
    private UUID subjectId = UUID.randomUUID();
    private UUID classroomId = UUID.randomUUID();

    public static TeachingAssignmentRequestBuilder builder() {
        return new TeachingAssignmentRequestBuilder();
    }

    public TeachingAssignmentRequestBuilder professorId(UUID professorId) {
        this.professorId = professorId;
        return this;
    }

    public TeachingAssignmentRequestBuilder subjectId(UUID subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    public TeachingAssignmentRequestBuilder classroomId(UUID classroomId) {
        this.classroomId = classroomId;
        return this;
    }

    public TeachingAssignmentRequest build() {
        return new TeachingAssignmentRequest(professorId, subjectId, classroomId);
    }

}
