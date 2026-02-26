package br.com.backend.domain;

public enum Role {
    ADMIN,
    STUDENT,
    PROFESSOR,
    CORDINATOR;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
