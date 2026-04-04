package br.com.backend.repository;

import br.com.backend.entity.Enrollment;
import br.com.backend.entity.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID>, JpaSpecificationExecutor<Enrollment> {

    long countByStatus(EnrollmentStatus status);
}
