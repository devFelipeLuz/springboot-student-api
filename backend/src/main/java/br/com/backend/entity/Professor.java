package br.com.backend.entity;

import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "professor")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private boolean active;

    public Professor(String name, User user) {
        this.name = validateName(name);
        this.user = Objects.requireNonNull(user, "User cannot be null");
        this.active = true;
    }

    public void deactivate() {
        ensureActive();
        this.active = false;
    }

    public void updateName(String name) {
        ensureActive();
        this.name = validateName(name);
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Name cannot be null or blank");
        }

        return name;
    }

    public void ensureActive() {
        if (!this.active) {
            throw  new BusinessException("Professor is not active");
        }
    }
}
