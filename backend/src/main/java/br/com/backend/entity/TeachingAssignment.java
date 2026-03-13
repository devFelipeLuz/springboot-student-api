package br.com.backend.entity;

import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

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
        if (professor == null) throw new BusinessException("professor cannot be null");
        if (subject == null) throw new BusinessException("subject cannot be null");
        if (classroom == null) throw new BusinessException("classroom cannot be null");

        this.professor = professor;
        this.subject = subject;
        this.classroom = classroom;
    }

    private void ensureProfessorIsActive() {
        if (!this.professor.isActive()) {
            throw new BusinessException("Professor is not active");
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
        ensureSchoolYearIsActive();
        ensureClassroomIsActive();
    }
}
