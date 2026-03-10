package br.com.backend.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
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
}
