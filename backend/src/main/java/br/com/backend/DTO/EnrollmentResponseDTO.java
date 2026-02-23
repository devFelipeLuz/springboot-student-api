package br.com.backend.DTO;


import br.com.backend.domain.Grade;
import br.com.backend.domain.Student;
import lombok.Getter;

import java.util.UUID;

public class EnrollmentResponseDTO {

    @Getter
    private UUID id;

    @Getter
    private Grade grade;

    @Getter
    private Student student;

    public EnrollmentResponseDTO(UUID id, Grade grade, Student student) {
        this.id = id;
        this.grade = grade;
        this.student = student;
    }
}
