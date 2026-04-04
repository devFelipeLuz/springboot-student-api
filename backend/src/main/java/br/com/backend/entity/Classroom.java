package br.com.backend.entity;

import br.com.backend.entity.enums.EnrollmentStatus;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

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

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @ManyToOne
    @JoinColumn(name = "school_year_id")
    private SchoolYear schoolYear;

    @Column(name = "active_enrollments", nullable = false)
    private int enrollmentCountForSchoolYear;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments =  new ArrayList<>();

    @Column(name = "active", nullable = false)
    private boolean active;

    public Classroom(String name, SchoolYear schoolYear) {
        schoolYear.ensureActive();
        this.name = validateName(name);
        this.maxCapacity = 25;
        this.schoolYear = Objects.requireNonNull(schoolYear, "School year cannot be null");
        this.enrollmentCountForSchoolYear = 0;
        this.active = true;
    }

    public void ensureCapacity() {
        if (enrollmentCountForSchoolYear >= maxCapacity) {
            throw new BusinessException("Classroom is full");
        }
    }

    public void changeCapacity(int newCapacity) {
        ensureActive();

        if (newCapacity < enrollmentCountForSchoolYear) {
            throw new BusinessException("The new capacity cannot be less than active enrollments");
        }

        this.maxCapacity = newCapacity;
    }

    public void addEnrollment(Enrollment enrollment) {
        ensureActive();

        if (enrollment == null) {
            throw new BusinessException("Enrollment cannot be null");
        }

        this.enrollments.add(enrollment);

        if (enrollment.getStatus() == EnrollmentStatus.ACTIVE) {
            increaseActiveEnrollmentsCount();
        }
    }

    public void increaseActiveEnrollmentsCount() {
        ensureCapacity();
        this.enrollmentCountForSchoolYear++;
    }

    public void decreaseActiveEnrollmentsCount() {
        ensureActive();

        if (enrollmentCountForSchoolYear == 0) {
            throw new IllegalArgumentException("Turma vazia");
        }

        this.enrollmentCountForSchoolYear--;
    }

    public List<Enrollment> getEnrollments() {
        return Collections.unmodifiableList(enrollments);
    }

    public void ensureActive() {
        if (!this.active) {
            throw new BusinessException("Classroom is not active");
        }
    }

    public void deactivate() {
        ensureActive();
        this.active = false;
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Name cannot be null or blank");
        }

        return name;
    }
}
