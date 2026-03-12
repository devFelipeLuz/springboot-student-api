package br.com.backend.entity;

import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
public class SchoolYear {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, updatable = false)
    private Instant startDate;

    @Column(updatable = false)
    private Instant endDate;

    @Column(name = "active", nullable = false)
    private boolean active;

    public SchoolYear(Integer year) {
        if (year == null || year <= 0) {
            throw new IllegalArgumentException("Year is null or blank");
        }

        this.year = year;
        this.startDate = Instant.now();
        this.endDate = null;
        this.active = true;
    }

    public void updateYear(Integer year) {
        this.year = year;
    }

    public void ensureActive() {
        if (!this.active) {
            throw new BusinessException("School year is inactive");
        }
    }

    public void deactivate() {
        if (!this.active) {
            throw new BusinessException("School year is already inactive");
        }

        this.active = false;
        this.endDate = Instant.now();
    }
}
