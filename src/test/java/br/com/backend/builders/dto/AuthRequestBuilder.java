package br.com.backend.builders.dto;

import br.com.backend.dto.request.AuthRequest;

public class AuthRequestBuilder {

    private String email = "admin@admin.com";
    private String password = "admin";

    public static AuthRequestBuilder builder() {
        return new AuthRequestBuilder();
    }

    public AuthRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public AuthRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public AuthRequest build() {
        return new AuthRequest(email, password);
    }
}
