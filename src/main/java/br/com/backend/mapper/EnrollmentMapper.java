package br.com.backend.mapper;


import br.com.backend.dto.response.EnrollmentResponseDTO;
import br.com.backend.entity.Classroom;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.SchoolYear;
import br.com.backend.entity.Student;

public final class EnrollmentMapper {

    private EnrollmentMapper() {
        throw new UnsupportedOperationException("Mapper");
    }

    public static EnrollmentResponseDTO toDTO(Enrollment enrollment) {
        Student student = enrollment.getStudent();
        SchoolYear schoolYear = enrollment.getSchoolYear();
        Classroom classroom = enrollment.getClassroom();

        return new EnrollmentResponseDTO(
                enrollment.getId(),
                student.getName(),
                schoolYear.getYear(),
                classroom.getName(),
                enrollment.getStatus()
        );
    }
}
