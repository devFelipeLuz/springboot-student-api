package br.com.backend.entity;

import br.com.backend.entity.enums.EnrollmentStatus;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Student(String name, User user) {
        this.name = validateName(name);
        this.user = Objects.requireNonNull(user, "User cannot be null");
        this.enrollments = new ArrayList<>();
        this.active = true;
    }

    public void updateName(String name) {
        ensureActive();
        this.name = validateName(name);
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean hasActiveEnrollment() {
        return enrollments.stream()
                .anyMatch(e -> e.getStatus() == EnrollmentStatus.ACTIVE);
    }

    public void ensureCanEnroll() {
        ensureActive();
        if (hasActiveEnrollment()) {
            throw new BusinessException("Student is already enrolled");
        }
    }

    public void addEnrollment(Enrollment enrollment) {
        ensureCanEnroll();
        this.enrollments.add(enrollment);
    }

    public Optional<Enrollment> getActiveEnrollment() {
        return this.enrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
                .findFirst();
    }

    public void ensureActive() {
        if (!this.active) {
            throw new BusinessException("Aluno inativo");
        }
    }

    public List<Enrollment> getEnrollments() {
        return Collections.unmodifiableList(this.enrollments);
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Student name is null or blank");
        }
        return name;
    }
}
