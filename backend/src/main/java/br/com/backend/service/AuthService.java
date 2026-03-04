package br.com.backend.service;

import br.com.backend.DTO.AuthRequest;
import br.com.backend.DTO.AuthResponse;
import br.com.backend.domain.User;
import br.com.backend.security.JwtService;
import br.com.backend.security.RefreshToken;
import br.com.backend.security.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final AuthenticationManager manager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    public AuthService(AuthenticationManager manager, JwtService jwtService, RefreshTokenService refreshTokenService){
        this.manager = manager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public AuthResponse login(AuthRequest request) {

        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refresh(String rawRefreshToken) {

        RefreshToken oldToken = refreshTokenService.getValidRefreshTokenOrThrow(rawRefreshToken);

        User user = oldToken.getUser();

        oldToken.revoke();

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    public void logout(String rawRefreshToken) {
        refreshTokenService.revokeRefreshToken(rawRefreshToken);
    }
}
