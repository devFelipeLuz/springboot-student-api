package br.com.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "professors")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "active")
    private boolean active;

    public Professor(String name, User user) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        this.name = name;
        this.user = user;
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
