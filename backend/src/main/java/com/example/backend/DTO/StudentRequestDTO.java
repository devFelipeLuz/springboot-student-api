package com.example.backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class StudentRequestDTO {

    @Getter
    @Setter
    @NotBlank(message = "Name is required")
    private String name;

    @Getter
    @Setter
    @Email
    @NotBlank
    private String email;

    @Getter
    @Setter
    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be positive")
    private Integer age;

    @Getter
    @Setter
    @NotBlank(message = "Grade is required")
    private String grade;

}
