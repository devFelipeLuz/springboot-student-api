package com.example.backend.repository;

import com.example.backend.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GradeRepository extends JpaRepository<Grade, UUID> {
}
