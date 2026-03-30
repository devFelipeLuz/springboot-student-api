package br.com.backend.repository;

import br.com.backend.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ClassroomRepository extends JpaRepository<Classroom, UUID>, JpaSpecificationExecutor<Classroom> {
}
