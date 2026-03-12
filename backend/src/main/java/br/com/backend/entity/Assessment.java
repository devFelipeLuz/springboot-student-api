package br.com.backend.entity;

import br.com.backend.entity.enums.AssessmentType;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private AssessmentType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teaching_assignments_id", nullable = false)
    private TeachingAssignment teachingAssignment;

    @Column(nullable = false, updatable = false)
    private Instant assessmentDate;

    public Assessment(TeachingAssignment teachingAssignment, String title, AssessmentType type) {
        validateInput(teachingAssignment, title, type);
        this.title = title;
        this.type = type;
        this.teachingAssignment = teachingAssignment;
        this.assessmentDate = Instant.now();
    }

    public void updateData(TeachingAssignment teachingAssignment, String title, AssessmentType type) {
        validateInput(teachingAssignment, title, type);
        this.title = title;
        this.type = type;
        this.teachingAssignment = teachingAssignment;
    }

    private void validateInput(TeachingAssignment teachingAssignment, String title, AssessmentType type) {
        if (teachingAssignment == null) {
            throw new BusinessException("TeachingAssignment cannot be null");
        }

        if (title == null || title.isBlank()) {
            throw new BusinessException("Title is null or blank");
        }

        if (type == null) {
            throw new BusinessException("Type is null or blank");
        }
    }
}
