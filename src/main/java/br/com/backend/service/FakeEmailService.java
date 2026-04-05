package br.com.backend.service;

import br.com.backend.exception.BusinessException;
import lombok.Getter;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Getter
@Component
@Primary
@Profile("test")
public class FakeEmailService implements EmailService {

    private String lastToken;
    private String lastEmail;

    @Override
    public void sendResetPasswordEmail(String token, String email) {
        this.lastToken = token;
        this.lastEmail = email;
    }

    public String getLastToken() {
        shouldThrowExceptionWhenTokenIsNull(lastToken);
        return lastToken;
    }

    private boolean verifyTokenIsNull(String token) {
        return token == null;
    }

    private void shouldThrowExceptionWhenTokenIsNull(String token) {
        if (verifyTokenIsNull(token)) {
            throw new BusinessException("Token is null");
        }
    }
}
