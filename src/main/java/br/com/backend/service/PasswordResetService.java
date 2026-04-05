package br.com.backend.service;

import br.com.backend.security.PasswordResetToken;
import br.com.backend.entity.User;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.PasswordResetTokenRepository;
import br.com.backend.repository.UserRepository;
import br.com.backend.security.TokenGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;

    public PasswordResetService(UserRepository userRepository,
                                PasswordResetTokenRepository repository,
                                PasswordEncoder passwordEncoder,
                                RefreshTokenService refreshTokenService,
                                EmailService emailService) {

        this.userRepository = userRepository;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.emailService = emailService;
    }

    public void requestPasswordReset(String email) {

        userRepository.findByEmail(email).ifPresent(user -> {

            String rawToken = TokenGenerator.generateSecureToken();
            String tokenHash = TokenGenerator.hashToken(rawToken);

            Instant expiresAt = Instant.now().plus(30, ChronoUnit.MINUTES);

            PasswordResetToken token = new PasswordResetToken(tokenHash, user, expiresAt);

            repository.save(token);
            emailService.sendResetPasswordEmail(rawToken, user.getEmail());
        });
    }

    public void resetPassword(String rawToken, String newPassword) {
        String tokenHash = TokenGenerator.hashToken(rawToken);

        PasswordResetToken token = repository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new EntityNotFoundException("Invalid token"));

        token.validateToken();

        User user = token.getUser();
        user.updatePassword(passwordEncoder.encode(newPassword));

        repository.deleteAllByUser(user);
        refreshTokenService.revokeAllUserRefreshTokens(user);
    }
}
