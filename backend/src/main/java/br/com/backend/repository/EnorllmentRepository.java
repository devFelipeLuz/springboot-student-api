package br.com.backend.repository;

import br.com.backend.domain.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EnorllmentRepository extends JpaRepository<Enrollment, UUID> {
    List<Enrollment> findAllByStatusActive();
}
