package br.com.backend.entity;

import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "student_grade",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"enrollment_id", "assessment_id"})
})
public class StudentGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(nullable = false)
    private Double grade;

    public StudentGrade(Assessment assessment, Enrollment enrollment) {
        if (assessment == null) {
            throw new NullPointerException("Assessment is null");
        }

        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment is null");
        }

        this.assessment = assessment;
        this.enrollment = enrollment;
        this.grade = null;
    }

    public void updateGrade(Double grade) {
        if (grade == null) {
            throw new BusinessException("Grade cannot be null");
        }

        if (this.enrollment == null) {
            throw new BusinessException("Enrollment cannot be null");
        }

        if (this.assessment == null) {
            throw new BusinessException("Assessment cannot be null");
        }

        this.grade = grade;
    }
}
