package br.com.backend.domain;

import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "grade")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "GRADE_NAME")
    private String name;

    @OneToMany(mappedBy = "grade")
    private List<Enrollment> enrollments = new ArrayList<>();

    @Column(name = "GRADE_CAPACITY")
    private Integer maxCapacity = 25;

    private Integer activeEnrollmentsCount;

    public Grade(String name) {
        this.name = name;
    }

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
