package br.com.backend.builders.dto;

import br.com.backend.dto.request.StudentUpdateRequest;

public class StudentUpdateRequestBuilder {
    private String name = null;
    private String email = null;
    private String password = null;

    public static StudentUpdateRequestBuilder builder() {
        return new StudentUpdateRequestBuilder();
    }

    public StudentUpdateRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public StudentUpdateRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public StudentUpdateRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public StudentUpdateRequest build() {
        return new StudentUpdateRequest(name, email, password);
    }
}
