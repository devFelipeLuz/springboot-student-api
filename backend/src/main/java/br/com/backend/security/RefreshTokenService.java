package br.com.backend.security;

import br.com.backend.domain.User;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private static final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    private String generateSecureToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA=256");
            byte[] hashBytes = md.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("SHA-256 not available", e);
        }
    }

    public String createRefreshToken(User user) {
        String rawToken = generateSecureToken();
        String tokenHash = hashToken(rawToken);

        Instant expiresDate = Instant.now().plus(7, ChronoUnit.DAYS);

        RefreshToken newToken = new RefreshToken(tokenHash, user, expiresDate);

        repository.save(newToken);

        return rawToken;
    }

    public RefreshToken getValidRefreshTokenOrThrow(String rawToken) {
        String tokenHash = hashToken(rawToken);

        RefreshToken token = repository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new EntityNotFoundException("Refresh token not found"));

        token.validateToken();

        return token;
    }

    public String rotateRefreshToken(String rawToken) {
        RefreshToken oldToken = getValidRefreshTokenOrThrow(rawToken);

        oldToken.revoke();

        return createRefreshToken(oldToken.getUser());
    }

    public void revokeRefreshToken(String rawToken) {
        String tokenHash = hashToken(rawToken);

        repository.findByTokenHash(tokenHash)
                .ifPresent(RefreshToken::revoke);
    }

    public void revokeAllUserRefreshTokens(User user){
        repository.revokeAllByUser(user);
    }
}
