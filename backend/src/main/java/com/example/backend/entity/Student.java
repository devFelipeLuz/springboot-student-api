package com.example.backend.entity;

import com.example.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name = "student")
public class Student {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Getter
    @Column(name = "STUDENT_NAME")
    private String name;

    @Getter
    @Column(name = "STUDENT_EMAIL")
    private String email;

    @Getter
    @Column(name = "STUDENT_AGE")
    private Integer age;

    @Getter
    @Column(name = "STUDENT_GRADE")
    private String grade;

    @Getter
    @Column(name = "ACTIVE")
    private Boolean active;

    public void updateData(String name, String email, Integer age, String grade) {
        if (!this.active) {
            throw new BusinessException("Aluno inativo");
        }

        if (age < 1) {
            throw new IllegalArgumentException("Idade inválida");
        }

        this.name = name;
        this.email = email;
        this.age = age;
        this.grade = grade;
    }

    public void deactivate() {
        this.active = false;
    }
}
