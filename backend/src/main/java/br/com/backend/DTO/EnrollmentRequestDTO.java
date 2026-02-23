package br.com.backend.DTO;

import br.com.backend.domain.Grade;
import br.com.backend.domain.Student;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class EnrollmentRequestDTO {

    @Getter
    @Setter
    @NotBlank(message = "Grade is required")
    private Grade grade;

    @Getter
    @Setter
    @NotBlank(message = "Student is required")
    private Student student;
}
