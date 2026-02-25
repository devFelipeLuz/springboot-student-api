package br.com.backend.DTO;

import lombok.Getter;

import java.util.UUID;

@Getter
public class StudentResponseDTO {

    private UUID id;

    private String name;

    private String email;

    private Integer age;

    private String grade;

    public StudentResponseDTO(UUID id, String name, String email, Integer age, String grade) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.grade = grade;
    }
}
