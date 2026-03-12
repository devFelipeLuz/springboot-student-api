package br.com.backend.entity;

import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "classroom")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "classroom_capacity")
    private int maxCapacity;

    @ManyToOne
    @JoinColumn(name = "school_year_id")
    private SchoolYear schoolYear;

    @Column(name = "active_enrollments")
    private int activeEnrollmentsCount;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;

    @Column(name = "active")
    private boolean active;

    public Classroom(String name, SchoolYear schoolYear) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Name cannot be null or blank");
        }

        if (schoolYear == null) {
            throw new BusinessException("School year cannot be null");
        }

        schoolYear.ensureActive();

        this.name = name;
        this.maxCapacity = 25;
        this.schoolYear = schoolYear;
        this.activeEnrollmentsCount = 0;
        this.enrollments = new ArrayList<>();
        this.active = true;
    }

    private void ensureCapacity() {
        if (activeEnrollmentsCount >= maxCapacity) {
            throw new BusinessException("Classroom is full");
        }
    }

    private void ensureSchoolYearIsActive() {
        if (!schoolYear.isActive()) {
            throw new BusinessException("School year is not active");
        }
    }

    public void ensureCanEnroll() {
        ensureCapacity();
        ensureSchoolYearIsActive();
    }

    public void changeCapacity(int newCapacity) {
        if (newCapacity < activeEnrollmentsCount) {
            throw new BusinessException("The new capacity cannot be less than active enrollments");
        }

        this.maxCapacity = newCapacity;
    }

    public void addEnrollment(Enrollment enrollment) {
        if (enrollment == null) {
            throw new BusinessException("Enrollment cannot be null");
        }

        this.enrollments.add(enrollment);
        increaseActiveEnrollmentsCount();
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

    public List<Enrollment> getEnrollments() {
        return Collections.unmodifiableList(enrollments);
    }

    public void deactivate() {
        this.active = false;
    }
}
