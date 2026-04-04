package br.com.backend.entity;

import br.com.backend.entity.enums.EnrollmentStatus;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "enrollment",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "classroom_id", "school_year_id"})
})
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    private SchoolYear schoolYear;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @Column(name = "enrollment_date", nullable = false, updatable = false)
    private Instant enrolledAt;

    @Column(name = "finish_date")
    private Instant finishedAt;

    @Column(name = "cancellation_date")
    private Instant canceledAt;

    public Enrollment(Student student, SchoolYear schoolYear, Classroom classroom) {
        this.student = Objects.requireNonNull(student, "Student must not be null");
        this.schoolYear = Objects.requireNonNull(schoolYear, "School year cannot be null");
        this.classroom = Objects.requireNonNull(classroom, "classroom cannot be null");
        this.status = EnrollmentStatus.ACTIVE;
        this.enrolledAt = Instant.now();
    }

    public boolean isActive() {
        return this.status == EnrollmentStatus.ACTIVE;
    }

    public boolean isCancelled() {
        return this.status == EnrollmentStatus.CANCELED
                && canceledAt != null;
    }

    public boolean isFinished() {
        return this.status == EnrollmentStatus.FINISHED
                && finishedAt != null;
    }

    public void finishEnrollment() {
        ensureAllActive();
        this.status = EnrollmentStatus.FINISHED;
        this.finishedAt = Instant.now();
    }

    public void cancelEnrollment() {
        ensureActive();
        this.classroom.decreaseActiveEnrollmentsCount();
        this.status = EnrollmentStatus.CANCELED;
        this.canceledAt = Instant.now();
    }

    public void ensureActive() {
        if (!this.isActive()) {
            throw new BusinessException("Esta matrícula não está ativa");
        }
    }

    public void ensureStudentActive() {
        if (!this.student.isActive()) {
            throw new BusinessException("Student is already inactive");
        }
    }

    public void ensureSchoolYearActive() {
        if (!this.schoolYear.isActive()) {
            throw new BusinessException("School year is already inactive");
        }
    }

    public void ensureClassroomActive() {
        if (!this.classroom.isActive()) {
            throw new BusinessException("Classroom is already inactive");
        }
    }

    public void ensureAllActive() {
        ensureActive();
        ensureStudentActive();
        ensureSchoolYearActive();
        ensureClassroomActive();
    }

    public void register() {
        ensureStudentActive();
        ensureClassroomActive();
        student.addEnrollment(this);
        classroom.addEnrollment(this);
    }
}