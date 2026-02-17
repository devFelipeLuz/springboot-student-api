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

    public Long activeEnrollmentsCount() {
        return enrollments.stream()
                .filter(e -> e.getStatus() == Enrollment.EnrollmentStatus.ACTIVE)
                .count();
    }

    public void validateCapacity() {
        if (activeEnrollmentsCount() >= maxCapacity) {
            throw new BusinessException("Turma lotada");
        }
    }
}
