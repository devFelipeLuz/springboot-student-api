package br.com.backend.DTO.student;

import br.com.backend.domain.Classroom;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StudentRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank(message = "Classroom is required")
    private Classroom classroom;
}
