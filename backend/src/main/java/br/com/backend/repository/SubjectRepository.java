package br.com.backend.repository;

import br.com.backend.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {
    Page<Subject> findAll(Pageable pageable);

    Page<Subject> findByActive(Boolean active, Pageable pageable);

    boolean existsByName(String name);
}
