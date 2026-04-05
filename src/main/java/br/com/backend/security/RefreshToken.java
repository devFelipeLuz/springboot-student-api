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
@Table(indexes = {
        @Index(name = "idx_revoked_revokedAt", columnList = "revoked, revokedAt"),
        @Index(name = "idx_expiresAt", columnList = "expiresAt")})
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 64)
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    private boolean revoked;

    private Instant revokedAt;

    public RefreshToken(String tokenHash, User user, Instant expiresAt) {
        this.tokenHash = tokenHash;
        this.user = user;
        this.createdAt = Instant.now();
        this.expiresAt = expiresAt;
        this.revoked = false;
        this.revokedAt = null;
    }

    public void validateToken() {
        if (this.revoked) {
            throw new BusinessException("Token has been revoked");
        }

        if (this.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException("Refresh token is expired");
        }

        if (this.getUser().getDeletedAt() != null) {
            throw new BusinessException("User has been deleted");
        }
    }

    public void revoke() {
        this.revoked = true;
        this.revokedAt = Instant.now();
    }
}
