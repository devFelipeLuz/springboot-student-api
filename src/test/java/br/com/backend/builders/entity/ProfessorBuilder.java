package br.com.backend.builders.entity;

import br.com.backend.entity.Professor;
import br.com.backend.entity.User;
import br.com.backend.entity.enums.Role;

public class ProfessorBuilder {

    private String name = "Professor Test";
    private User user = User.createUser(
            "professor@test.com", "password", Role.PROFESSOR);

    public static ProfessorBuilder builder() {
        return new ProfessorBuilder();
    }

    public ProfessorBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProfessorBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public Professor build() {
        return new Professor(name, user);
    }
}
