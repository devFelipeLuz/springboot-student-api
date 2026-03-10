package br.com.backend.repository;

import br.com.backend.domain.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    Page<Enrollment> findByStatusActive(Pageable pageable);

    Page<Enrollment> findAll(Pageable pageable);
}
