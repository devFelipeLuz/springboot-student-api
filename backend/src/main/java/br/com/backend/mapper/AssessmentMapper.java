package br.com.backend.mapper;

import br.com.backend.dto.response.AssessmentResponseDTO;
import br.com.backend.entity.*;

public final class AssessmentMapper {

    private AssessmentMapper() {
        throw new UnsupportedOperationException("Mapper");
    }

    public static AssessmentResponseDTO toDTO(Assessment assessment) {
        TeachingAssignment assignment = assessment.getTeachingAssignment();
        Subject subject = assignment.getSubject();
        Professor professor = assignment.getProfessor();
        Classroom classroom = assignment.getClassroom();

        return new AssessmentResponseDTO(
                assessment.getId(),
                assessment.getTitle(),
                subject.getName(),
                assessment.getType(),
                professor.getName(),
                classroom.getName(),
                assessment.getDate()
        );
    }
}
