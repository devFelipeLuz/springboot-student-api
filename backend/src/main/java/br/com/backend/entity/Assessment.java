package br.com.backend.entity;

import br.com.backend.entity.enums.AssessmentType;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;
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
        this.title = validateTitle(title);
        this.type = validateType(type);
        this.teachingAssignment = Objects.requireNonNull(
                teachingAssignment, "Teaching Assignment cannot be null");
        this.assessmentDate = Instant.now();
    }

    public void updateTitle(String title) {
        this.title = validateTitle(title);
    }

    public void updateType(AssessmentType type) {
        this.type = validateType(type);
    }

    private String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new BusinessException("Name cannot be null or blank");
        }
        return title;
    }

    private AssessmentType validateType(AssessmentType type) {
        if (type == null) {
            throw new BusinessException("Type cannot be null");
        }

        if (!type.equals(AssessmentType.TRABALHO) &&
            !type.equals(AssessmentType.PROVA) &&
            !type.equals(AssessmentType.LIÇÃO)) {
            throw new BusinessException("Type must be TRABALHO, PROVA or LIÇÃO");
        }
        return type;
    }
}
