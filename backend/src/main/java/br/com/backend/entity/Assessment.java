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
@Table(name = "assessment")
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

    @Column(nullable = false)
    private boolean active;

    public Assessment(TeachingAssignment teachingAssignment, String title, AssessmentType type) {
        this.title = validateTitle(title);
        this.type = validateType(type);
        this.teachingAssignment = Objects.requireNonNull(
                teachingAssignment, "Teaching Assignment cannot be null");
        this.assessmentDate = Instant.now();
        this.active = true;
    }

    public void updateTitle(String title) {
        ensureActive();
        this.title = validateTitle(title);
    }

    public void updateType(AssessmentType type) {
        ensureActive();
        this.type = validateType(type);
    }

    public void ensureActive() {
        if (!this.active) {
            throw new BusinessException("Assessment is not active");
        }
    }

    public void deactivate() {
        ensureActive();
        this.active = false;
    }

    private String validateTitle(String title) {
        throwsBusinessExceptionWithInvalidString(title);
        return title;
    }

    private boolean ensureStringIsNotNull(String string) {
        return string != null;
    }

    private boolean ensureStringIsNotEmpty(String string) {
        return !string.isBlank();
    }

    private void throwsBusinessExceptionWithInvalidString(String string) {
        if (!ensureStringIsNotNull(string) && !ensureStringIsNotEmpty(string)) {
            throw new BusinessException("Name cannot be null or blank");
        }
    }

    private AssessmentType validateType(AssessmentType type) {
        if (type == null) {
            throw new BusinessException("Type cannot be null");
        }

        return type;
    }
}
