package br.com.backend.repository;

import br.com.backend.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GradeRepository extends JpaRepository<Grade, UUID> {
}
