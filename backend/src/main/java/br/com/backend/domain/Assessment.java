package br.com.backend.domain;

import br.com.backend.domain.enums.AssessmentType;
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
    private Instant date;

    public Assessment(String title, AssessmentType type, TeachingAssignment teachingAssignment) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is null or blank");
        }

        if (type == null) {
            throw new IllegalArgumentException("Type is null or blank");
        }

        if (teachingAssignment == null) {
            throw new IllegalArgumentException("TeachingAssignment is null or blank");
        }

        this.title = title;
        this.type = type;
        this.teachingAssignment = teachingAssignment;
        this.date = Instant.now();
    }

    public void updateData(String title, AssessmentType type, TeachingAssignment teachingAssignment) {
        this.title = title;
        this.type = type;
        this.teachingAssignment = teachingAssignment;
    }
}
