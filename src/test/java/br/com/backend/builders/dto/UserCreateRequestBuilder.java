package br.com.backend.builders.dto;

import br.com.backend.dto.request.UserCreateRequest;
import br.com.backend.entity.enums.Role;

public class UserCreateRequestBuilder {

    private String email = "test@test.com";
    private String password = "123456";
    private Role role = Role.STUDENT;

    public static UserCreateRequestBuilder builder() {
        return new UserCreateRequestBuilder();
    }

    public UserCreateRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserCreateRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserCreateRequestBuilder withRole(Role role) {
        this.role = role;
        return this;
    }

    public UserCreateRequest build() {
        return new UserCreateRequest(email, password, role);
    }
}
