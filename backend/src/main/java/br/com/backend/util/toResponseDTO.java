package br.com.backend.util;

import br.com.backend.DTO.assessment.AssessmentResponseDTO;
import br.com.backend.DTO.enrollment.EnrollmentResponseDTO;
import br.com.backend.DTO.classroom.ClassroomResponseDTO;
import br.com.backend.DTO.grade.StudentGradeResponseDTO;
import br.com.backend.DTO.professor.ProfessorResponseDTO;
import br.com.backend.DTO.student.StudentResponseDTO;
import br.com.backend.DTO.user.UserResponseDTO;
import br.com.backend.domain.*;

public final class ToResponseDTO {

    private ToResponseDTO() {
        throw new UnsupportedOperationException("Classe de utilitários");
    }

    public static UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }

    public static EnrollmentResponseDTO toEnrollmentResponseDTO(Enrollment enrollment) {
        return new EnrollmentResponseDTO(
                enrollment.getId(),
                enrollment.getStudent(),
                enrollment.getClassroom(),
                enrollment.getSchoolYear()
        );
    }

    public static ClassroomResponseDTO toClassroomResponseDTO(Classroom classroom) {
        return new ClassroomResponseDTO(
                classroom.getId(),
                classroom.getName());
    }

    public static StudentResponseDTO toStudentResponseDTO(Student student) {
        String classroomName = student.getActiveEnrollments()
                .map(e -> e.getClassroom().getName())
                .orElse(null);

        User user = student.getUser();

        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                user.getEmail(),
                classroomName
        );
    }

    public static AssessmentResponseDTO toAssessmentResponseDTO(Assessment assessment) {
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

    public static StudentGradeResponseDTO toStudentGradeResponseDTO(StudentGrade studentGrade) {
        Assessment assessment = studentGrade.getAssessment();
        Student student = studentGrade.getEnrollment().getStudent();
        Professor professor = assessment.getTeachingAssignment().getProfessor();
        Subject subject = assessment.getTeachingAssignment().getSubject();

        return new StudentGradeResponseDTO(
                studentGrade.getId(),
                student.getName(),
                studentGrade.getGrade(),
                assessment.getTitle(),
                assessment.getType(),
                assessment.getDate(),
                professor.getName(),
                subject.getName()
        );
    }

    public static ProfessorResponseDTO toProfessorResponseDTO(Professor professor) {
        User user = professor.getUser();

        return new ProfessorResponseDTO(
                professor.getId(),
                professor.getName(),
                user.getUsername()
        );
    }
}
