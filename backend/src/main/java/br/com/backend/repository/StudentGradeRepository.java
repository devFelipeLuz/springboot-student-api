package br.com.backend.repository;

import br.com.backend.domain.StudentGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentGradeRepository extends JpaRepository<StudentGrade, UUID> {
    Page<StudentGrade> findAll(Pageable pageable);

    boolean existsByAssessmentIdAndEnrollmentId(UUID assessmentId, UUID enrollmentId);

    Page<StudentGrade> findByAssessmentId(UUID assessmentId, Pageable pageable);
}
