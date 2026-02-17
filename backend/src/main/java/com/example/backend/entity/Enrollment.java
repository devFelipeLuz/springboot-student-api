package com.example.backend.entity;

import com.example.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    public Enrollment() {

    }

    public Enrollment(Student student, Grade grade) {
        if (student == null) {
            throw new IllegalArgumentException("Student não pode ser null");
        }

        if (grade == null) {
            throw new IllegalArgumentException("Grade não pode ser null");
        }

        this.student = student;
        this.grade = grade;
        this.status = EnrollmentStatus.ACTIVE;
        this.enrollmentDate = LocalDate.now();

        student.getEnrollments().add(this);
        grade.getEnrollments().add(this);
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Getter
    @ManyToOne
    private Grade grade;

    @Getter
    @ManyToOne
    private Student student;

    @Getter
    @Column(name = "ENROLLMENT_DATE")
    private LocalDate enrollmentDate;

    @Getter
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    public enum EnrollmentStatus {
        ACTIVE,
        CANCELED,
    }

    public void cancel() {
        if (this.status != EnrollmentStatus.ACTIVE) {
            throw new BusinessException("Esta matrícula não está ativa");
        }

        this.status = EnrollmentStatus.CANCELED;
    }
}