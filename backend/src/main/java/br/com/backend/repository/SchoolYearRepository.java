package br.com.backend.repository;

import br.com.backend.entity.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SchoolYearRepository extends JpaRepository<SchoolYear, UUID>, JpaSpecificationExecutor<SchoolYear> {
}
