package br.com.backend.util;

import br.com.backend.DTO.EnrollmentResponseDTO;
import br.com.backend.DTO.GradeResponseDTO;
import br.com.backend.DTO.StudentResponseDTO;
import br.com.backend.DTO.UserResponseDTO;
import br.com.backend.domain.Enrollment;
import br.com.backend.domain.Grade;
import br.com.backend.domain.Student;
import br.com.backend.domain.User;

public final class FunctionsUtils {

    private FunctionsUtils() {
        throw new UnsupportedOperationException("Classe de utilitários");
    }

    public static UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }

    public static EnrollmentResponseDTO enrollmentToResponseDTO(Enrollment enrollment) {
        return new EnrollmentResponseDTO(
                enrollment.getId(),
                enrollment.getGrade(),
                enrollment.getStudent()
        );
    }

    public static GradeResponseDTO toGradeResponseDTO(Grade grade) {
        return new GradeResponseDTO(
                grade.getId(),
                grade.getName());
    }

    public static StudentResponseDTO toStudentResponseDTO(Student student) {
        String gradeName = student.getActiveEnrollments()
                .map(e -> e.getGrade().getName())
                .orElse(null);

        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getAge(),
                gradeName
        );
    }
}
