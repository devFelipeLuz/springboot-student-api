package br.com.backend.entity;

import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean active;

    public Subject(String name) {
        this.name = validateName(name);
        this.active = true;
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Subject name cannot be null or blank");
        }

        return name.trim();
    }

    public void ensureActive() {
        if (!this.active) {
            throw new BusinessException("Subject is not active");
        }
    }

    public void updateName(String name) {
        ensureActive();
        this.name = validateName(name);
    }

    public void deactivate() {
        ensureActive();
        this.active = false;
    }
}
