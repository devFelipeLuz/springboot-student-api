package br.com.backend.entity;

import br.com.backend.entity.enums.EnrollmentStatus;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "enrollment",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "classroom_id"})
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

    @Column(name = "cancellation_date")
    private Instant canceledAt;

    public Enrollment(Student student, Classroom classroom, SchoolYear schoolYear) {
        if (student == null) {
            throw new IllegalArgumentException("Student não pode ser null");
        }

        if (classroom == null) {
            throw new IllegalArgumentException("Classroom não pode ser null");
        }

        if (schoolYear == null) {
            throw  new IllegalArgumentException("SchoolYear não pode ser null");
        }

        this.student = student;
        this.classroom = classroom;
        this.schoolYear = schoolYear;
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
                && canceledAt != null;
    }

    public void finishEnrollment() {
        ensureActive();
        this.status = EnrollmentStatus.FINISHED;
        this.canceledAt = Instant.now();
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

    public void register() {
        student.addEnrollment(this);
        classroom.addEnrollment(this);
    }
}