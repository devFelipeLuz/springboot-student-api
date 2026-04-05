package br.com.backend.builders.entity;

import br.com.backend.entity.User;
import br.com.backend.entity.enums.Role;

public class UserBuilder {

    private String email = "user@test.com";
    private String password = "password";
    private Role role = Role.STUDENT;

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withRole(Role role) {
        this.role = role;
        return this;
    }

    public User build() {
        return new User(email, password, role);
    }
}
