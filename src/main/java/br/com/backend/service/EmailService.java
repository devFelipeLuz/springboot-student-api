package br.com.backend.service;

public interface EmailService {
    void sendResetPasswordEmail(String token, String email);
}
