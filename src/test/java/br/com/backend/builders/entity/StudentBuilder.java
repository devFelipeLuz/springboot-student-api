package br.com.backend.builders.entity;

import br.com.backend.entity.Student;
import br.com.backend.entity.User;
import br.com.backend.entity.enums.Role;

public class StudentBuilder {

    private String name = "Student Test";
    private User user = User.createUser(
            "student@test.com", "studentPassword", Role.STUDENT);

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public StudentBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public StudentBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public Student build() {
        return new Student(name, user);
    }
}
