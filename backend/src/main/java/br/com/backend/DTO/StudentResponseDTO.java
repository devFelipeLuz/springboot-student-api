package br.com.backend.DTO;

import lombok.Getter;

import java.util.UUID;

public class StudentResponseDTO {

    @Getter
    private UUID id;

    @Getter
    private String name;

    @Getter
    private String email;

    @Getter
    private Integer age;

    @Getter
    private String grade;

    public StudentResponseDTO(UUID id, String name, String email, Integer age, String grade) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.grade = grade;
    }
}
