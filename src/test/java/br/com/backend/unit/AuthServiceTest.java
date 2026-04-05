package br.com.backend.unit;

import br.com.backend.builders.entity.UserBuilder;
import br.com.backend.dto.request.AuthRequest;
import br.com.backend.dto.response.AuthResponse;
import br.com.backend.entity.User;
import br.com.backend.security.JwtService;
import br.com.backend.security.RefreshToken;
import br.com.backend.service.AuthService;
import br.com.backend.service.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager manager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService service;

    private User user;

    @BeforeEach
    void setUp() {
        user = UserBuilder.builder().build();
    }

    @Test
    void shouldLoginSuccessfully() {

        AuthRequest request = new AuthRequest(user.getUsername(), user.getPassword());

        Authentication auth = mock(Authentication.class);

        when(manager.authenticate(any()))
                .thenReturn(auth);

        when(auth.getPrincipal())
                .thenReturn(user);

        when(jwtService.generateToken(user))
                .thenReturn("access-token");

        when(refreshTokenService.createRefreshToken(user))
                .thenReturn("refresh-token");

        AuthResponse response = service.login(request);

        verify(manager).authenticate(any());
        verify(refreshTokenService).enforceActiveTokenLimit(user);
        verify(jwtService).generateToken(user);
        verify(refreshTokenService).createRefreshToken(user);

        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
    }

    @Test
    void shouldRefreshToken() {
        String rawToken = "refresh-token";

        RefreshToken oldToken = mock(RefreshToken.class);

        when(refreshTokenService.getValidRefreshTokenOrThrow(rawToken))
                .thenReturn(oldToken);

        when(oldToken.getUser())
                .thenReturn(user);

        when(jwtService.generateToken(user))
                .thenReturn("new-access-token");

        when(refreshTokenService.createRefreshToken(user))
                .thenReturn("new-refresh-token");

        AuthResponse response = service.refresh(rawToken);

        verify(oldToken).revoke();
        verify(jwtService).generateToken(user);
        verify(refreshTokenService).createRefreshToken(user);

        assertEquals("new-access-token", response.accessToken());
        assertEquals("new-refresh-token", response.refreshToken());
    }

    @Test
    void shouldLogoutSuccessfully() {
        String rawToken = "refresh-token";

        service.logout(rawToken);

        verify(refreshTokenService).revokeRefreshToken(rawToken);
    }

    @Test
    void shouldThrowExceptionWhenAuthenticationFails() {
        AuthRequest request =
                new AuthRequest("user@test.com", "wrong");

        when(manager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(
                BadCredentialsException.class,
                () -> service.login(request));
    }
}
