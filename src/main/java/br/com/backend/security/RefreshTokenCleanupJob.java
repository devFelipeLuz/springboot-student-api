package br.com.backend.security;

import br.com.backend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupJob {

    private final RefreshTokenRepository repository;

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanOldRefreshTokens() {
        Instant threshold =
                Instant.now().minus(30, ChronoUnit.DAYS);

        repository.deleteOldTokens(threshold);
    }
}
