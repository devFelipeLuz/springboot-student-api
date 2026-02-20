package com.example.backend.entity;

import com.example.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "grade")
public class Grade {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Getter
    @Column(name = "GRADE_NAME")
    private String name;

    @Getter
    @OneToMany(mappedBy = "grade")
    private List<Enrollment> enrollments = new ArrayList<>();

    @Getter
    @Column(name = "GRADE_CAPACITY")
    private Integer maxCapacity = 25;

    @Getter
    private Integer activeEnrollmentsCount;

    public void validateCapacity() {
        if (activeEnrollmentsCount >= maxCapacity) {
            throw new BusinessException("Turma lotada");
        }
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
    }

    public void increaseActiveEnrollmentsCount() {
        if (activeEnrollmentsCount >= maxCapacity) {
            throw new BusinessException("Turma lotada");
        }

        this.activeEnrollmentsCount++;
    }

    public void decreaseActiveEnrollmentsCount() {
        if (activeEnrollmentsCount == 0) {
            throw new IllegalArgumentException("Turma vazia");
        }

        this.activeEnrollmentsCount--;
    }

    public void cancelEnrollment(Enrollment enrollment) {
        enrollment.cancel();
        this.decreaseActiveEnrollmentsCount();
    }
}
