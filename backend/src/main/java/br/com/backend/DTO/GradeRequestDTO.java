package br.com.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GradeRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;
}
