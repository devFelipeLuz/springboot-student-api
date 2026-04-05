package br.com.backend.repository;

import br.com.backend.entity.User;
import br.com.backend.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.user = :user")
    void revokeAllByUser(@Param("user") User user);

    @Modifying
    @Query("""
    DELETE FROM RefreshToken rt
    WHERE (rt.revoked = true AND rt.revokedAt < :threshold)
    OR (rt.expiresAt < :threshold)
    """)
    void deleteOldTokens(Instant threshold);

    List<RefreshToken> findByUserAndRevokedFalseOrderByCreatedAtAsc(User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user.id = :userId")
    void deleteAllByUserId(UUID userId);
}
