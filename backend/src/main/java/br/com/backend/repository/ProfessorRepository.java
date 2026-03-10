package br.com.backend.repository;

import br.com.backend.domain.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfessorRepository extends JpaRepository<Professor, UUID> {

    Page<Professor> findAll(Pageable pageable);

}
