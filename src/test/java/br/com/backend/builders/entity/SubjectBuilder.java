package br.com.backend.builders.entity;

import br.com.backend.entity.Subject;

public class SubjectBuilder {

    private String name = "História";

    public static SubjectBuilder builder() {
        return new SubjectBuilder();
    }

    public SubjectBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Subject build() {
        return new Subject(name);
    }
}
