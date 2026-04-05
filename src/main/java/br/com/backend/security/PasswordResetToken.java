package br.com.backend.security;

import br.com.backend.entity.User;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    private boolean used;

    public PasswordResetToken(String tokenHash, User user, Instant expiresAt) {
        this.tokenHash = tokenHash;
        this.user = user;
        this.createdAt = Instant.now();
        this.expiresAt = expiresAt;
        this.used = false;
    }

    public void validateToken() {
        if (this.used) {
            throw new BusinessException("Password reset token is already used");
        }

        if (this.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException("Password reset token expired");
        }

        if (this.getUser().getDeletedAt() != null) {
            throw new BusinessException("User has been deleted");
        }
    }

    public void markAsUsed() {
        this.used = true;
    }
}
