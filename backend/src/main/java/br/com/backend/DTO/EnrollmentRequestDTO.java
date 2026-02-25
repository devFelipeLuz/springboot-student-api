package br.com.backend.DTO;

import br.com.backend.domain.Grade;
import br.com.backend.domain.Student;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EnrollmentRequestDTO {

    @NotBlank(message = "Grade is required")
    private Grade grade;

    @NotBlank(message = "Student is required")
    private Student student;
}
