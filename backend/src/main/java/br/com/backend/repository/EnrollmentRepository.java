package br.com.backend.repository;

import br.com.backend.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID>, JpaSpecificationExecutor<Enrollment> {
}
