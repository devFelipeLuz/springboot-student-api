package br.com.backend.service;

import br.com.backend.entity.User;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.RefreshTokenRepository;
import br.com.backend.security.RefreshToken;
import br.com.backend.security.TokenGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository repository;


    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public String createRefreshToken(User user) {
        String rawToken = TokenGenerator.generateSecureToken();
        String tokenHash = TokenGenerator.hashToken(rawToken);

        Instant expiresDate = Instant.now().plus(7, ChronoUnit.DAYS);

        RefreshToken newToken = new RefreshToken(tokenHash, user, expiresDate);

        repository.save(newToken);

        return rawToken;
    }

    public RefreshToken getValidRefreshTokenOrThrow(String rawToken) {
        String tokenHash = TokenGenerator.hashToken(rawToken);

        RefreshToken token = repository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new EntityNotFoundException("Refresh token not found"));

        token.validateToken();

        return token;
    }

    public void revokeRefreshToken(String rawToken) {
        String tokenHash = TokenGenerator.hashToken(rawToken);

        repository.findByTokenHash(tokenHash)
                .ifPresent(RefreshToken::revoke);
    }

    public void revokeAllUserRefreshTokens(User user){
        repository.revokeAllByUser(user);
    }

    public void enforceActiveTokenLimit(User user) {
        List<RefreshToken> activeTokens = repository.findByUserAndRevokedFalseOrderByCreatedAtAsc(user);

        if (activeTokens.size() >= 3) {
            RefreshToken oldest = activeTokens.getFirst();
            oldest.revoke();
        }
    }
}
