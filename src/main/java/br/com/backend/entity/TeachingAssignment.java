package br.com.backend.entity;

import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "teaching_assignments",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"professor_id", "subject_id", "classroom_id"})
})
public class TeachingAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    public TeachingAssignment (Professor professor, Subject subject, Classroom classroom) {
        this.professor = Objects.requireNonNull(professor, "professor cannot be null");
        this.subject = Objects.requireNonNull(subject, "subject cannot be null");
        this.classroom = Objects.requireNonNull(classroom, "classroom cannot be null");
    }

    private void ensureProfessorIsActive() {
        if (!this.professor.isActive()) {
            throw new BusinessException("Professor is not active");
        }
    }

    private void ensureSubjectIsActive() {
        if (!this.subject.isActive()) {
            throw new BusinessException("Subject is not active");
        }
    }

    private void ensureSchoolYearIsActive() {
        if (!this.classroom.getSchoolYear().isActive()) {
            throw new BusinessException("School year is not active");
        }
    }

    private void ensureClassroomIsActive() {
        if (!this.classroom.isActive()) {
            throw new BusinessException("Classroom is not active");
        }
    }

    public void ensureAllIsActive() {
        ensureProfessorIsActive();
        ensureSubjectIsActive();
        ensureSchoolYearIsActive();
        ensureClassroomIsActive();
    }
}
