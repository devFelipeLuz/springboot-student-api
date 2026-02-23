package br.com.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class GradeRequestDTO {

    @Getter
    @NotBlank(message = "Name is required")
    private String name;
}
