package br.com.backend.builders.entity;

import br.com.backend.entity.Classroom;
import br.com.backend.entity.SchoolYear;

public class ClassroomBuilder {

    private String name = "3.A";
    private SchoolYear schoolYear = new SchoolYear(1989);

    public static ClassroomBuilder builder() {
        return new ClassroomBuilder();
    }

    public ClassroomBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ClassroomBuilder withSchoolYear(SchoolYear schoolYear) {
        this.schoolYear = schoolYear;
        return this;
    }

    public Classroom build() {
        return new Classroom(name, schoolYear);
    }
}
