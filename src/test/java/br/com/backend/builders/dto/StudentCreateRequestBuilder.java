package br.com.backend.builders.dto;

import br.com.backend.dto.request.StudentCreateRequest;

public class StudentCreateRequestBuilder {

    private String name = "Ricardo Cruz";
    private String email = "ricardo.cruz@email.com";
    private String password = "password";

    public static StudentCreateRequestBuilder builder() {
        return new StudentCreateRequestBuilder();
    }

    public StudentCreateRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public StudentCreateRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public StudentCreateRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public StudentCreateRequest build() {
        return new StudentCreateRequest(name, email, password);
    }
}
