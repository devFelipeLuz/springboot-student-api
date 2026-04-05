package br.com.backend.builders.entity;

import br.com.backend.entity.SchoolYear;

public class SchoolYearBuilder {

    private Integer year = 1989;

    public static SchoolYearBuilder builder() {
        return new SchoolYearBuilder();
    }

    public SchoolYearBuilder withYear(Integer year) {
        this.year = year;
        return this;
    }

    public SchoolYear build() {
        return new SchoolYear(year);
    }
}
