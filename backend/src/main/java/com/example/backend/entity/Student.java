package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "student")
public class Student {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Getter
    @Setter
    @Column(name = "STUDENT_NAME")
    private String name;

    @Getter
    @Setter
    @Column(name = "STUDENT_EMAIL")
    private String email;

    @Getter
    @Setter
    @Column(name = "STUDENT_AGE")
    private Integer age;

    @Getter
    @Setter
    @Column(name = "STUDENT_GRADE")
    private String grade;

}
