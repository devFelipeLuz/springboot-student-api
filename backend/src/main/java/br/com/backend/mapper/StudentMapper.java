package br.com.backend.mapper;

import br.com.backend.dto.response.StudentResponseDTO;
import br.com.backend.entity.Student;
import br.com.backend.entity.User;

public final class StudentMapper {

    private StudentMapper() {
        throw new UnsupportedOperationException("Mapper");
    }

    public static StudentResponseDTO toDTO(Student student) {
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
}
