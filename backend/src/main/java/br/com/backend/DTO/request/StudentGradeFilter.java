package br.com.backend.dto.request;

import br.com.backend.entity.enums.AssessmentType;

public record StudentGradeFilter(
        AssessmentType assessmentType,
        String studentName,
        String professorName,
        String subjectName,
        String classroomName
) {
}
