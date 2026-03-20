package br.com.backend.repository;

import br.com.backend.entity.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfessorRepository extends JpaRepository<Professor, UUID> {
    Page<Professor> findAll(Pageable pageable);

    Page<Professor> findByActive(Boolean active, Pageable pageable);
}
