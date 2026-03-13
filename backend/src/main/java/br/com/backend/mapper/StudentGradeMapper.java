package br.com.backend.mapper;

import br.com.backend.dto.response.StudentGradeResponseDTO;
import br.com.backend.entity.*;

public final class StudentGradeMapper {

    private StudentGradeMapper() {
        throw new UnsupportedOperationException("Mapper");
    }

    public static StudentGradeResponseDTO toDTO(StudentGrade studentGrade) {
        Assessment assessment = studentGrade.getAssessment();
        Student student = studentGrade.getEnrollment().getStudent();
        Professor professor = assessment.getTeachingAssignment().getProfessor();
        Subject subject = assessment.getTeachingAssignment().getSubject();

        return new StudentGradeResponseDTO(
                studentGrade.getId(),
                student.getName(),
                studentGrade.getGrade(),
                studentGrade.getMaxScore(),
                assessment.getTitle(),
                assessment.getType(),
                assessment.getAssessmentDate(),
                professor.getName(),
                subject.getName()
        );
    }
}
