package br.com.backend.controller;

import br.com.backend.dto.request.ForgotPasswordRequest;
import br.com.backend.dto.request.ResetPasswordRequest;
import br.com.backend.dto.request.AuthRequest;
import br.com.backend.dto.response.AuthResponse;
import br.com.backend.dto.request.RefreshRequest;
import br.com.backend.service.AuthService;
import br.com.backend.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService,  PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }

    @Operation(
            summary = "Login user",
            description = "Retorna access token e refresh token"
    )
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @Operation(summary = "Request refresh token")
    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {
        return authService.refresh(request.refreshToken());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Logout user")
    @PostMapping("/logout")
    public void logout(@RequestBody RefreshRequest request) {
        authService.logout(request.refreshToken());
    }

    @Operation(summary = "Request reset password")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.requestPasswordReset(request.email());
        return ResponseEntity.ok("If the account exists, you will receive an email");
    }

    @Operation(summary = "Reset password")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok("Password successfully reset");
    }
}
