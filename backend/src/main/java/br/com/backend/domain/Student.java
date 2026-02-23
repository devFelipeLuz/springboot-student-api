package br.com.backend.domain;

import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;

    @Getter
    @Column(name = "ACTIVE")
    private Boolean active;

    protected Student() {}

    public Student(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.enrollments = new ArrayList<>();
        this.active = true;
    }

    public void saveData(String name, String email, Integer age) {
        if (!this.active) {
            throw new BusinessException("Aluno inativo");
        }

        if (age < 1) {
            throw new IllegalArgumentException("Idade inválida");
        }

        this.name = name;
        this.email = email;
        this.age = age;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean hasActiveEnrollment() {
        return enrollments.stream()
                .anyMatch(e -> e.getStatus() == Enrollment.EnrollmentStatus.ACTIVE);
    }

    public void validateCanEnroll() {
        if (!this.active) {
            throw new BusinessException("Aluno inativo");
        }

        if (hasActiveEnrollment()) {
            throw new BusinessException("Aluno já possui matrícula ativa");
        }
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
    }

    public Optional<Enrollment> getActiveEnrollments() {
        return this.enrollments.stream()
                .filter(e -> e.getStatus() == Enrollment.EnrollmentStatus.ACTIVE)
                .findFirst();
    }
}
