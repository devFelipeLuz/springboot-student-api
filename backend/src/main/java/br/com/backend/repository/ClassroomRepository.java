package br.com.backend.repository;

import br.com.backend.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClassroomRepository extends JpaRepository<Classroom, UUID> {
    Page<Classroom> findByActive(Boolean active, Pageable pageable);

    Page<Classroom> findAll(Pageable pageable);
}
