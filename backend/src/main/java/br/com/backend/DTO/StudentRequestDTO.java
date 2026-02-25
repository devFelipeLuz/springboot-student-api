package br.com.backend.DTO;

import br.com.backend.domain.Grade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StudentRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be positive")
    private Integer age;

    @NotBlank(message = "Grade is required")
    private Grade grade;
}
