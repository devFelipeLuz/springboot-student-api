package br.com.backend.security;

import br.com.backend.domain.User;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 64)
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Instant createdAt;

    private Instant expiresAt;

    @Setter
    private boolean revoked;

    public RefreshToken(String tokenHash, User user, Instant expiresAt) {
        this.tokenHash = tokenHash;
        this.user = user;
        this.createdAt = Instant.now();
        this.expiresAt = expiresAt;
        this.revoked = false;
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
    }
}
