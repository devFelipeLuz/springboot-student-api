package br.com.backend.repository;

import br.com.backend.entity.StudentGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface StudentGradeRepository extends JpaRepository<StudentGrade, UUID>, JpaSpecificationExecutor<StudentGrade> {
    boolean existsByAssessmentIdAndEnrollmentId(UUID assessmentId, UUID enrollmentId);

    Page<StudentGrade> findByAssessmentId(UUID assessmentId, Pageable pageable);
}
