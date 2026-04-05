package br.com.backend.builders.entity;

import br.com.backend.entity.Assessment;
import br.com.backend.entity.TeachingAssignment;
import br.com.backend.entity.enums.AssessmentType;

public class AssessmentBuilder {

    private TeachingAssignment assignment = TeachingAssignmentBuilder.builder().build();
    private String title = "Prova de História";
    private AssessmentType type = AssessmentType.PROVA;

    public static AssessmentBuilder builder() {
        return new AssessmentBuilder();
    }

    public AssessmentBuilder withAssignment(TeachingAssignment assignment) {
        this.assignment = assignment;
        return this;
    }

    public AssessmentBuilder title(String title) {
        this.title = title;
        return this;
    }

    public AssessmentBuilder withType(AssessmentType type) {
        this.type = type;
        return this;
    }

    public Assessment build() {
        return new Assessment(assignment,  title, type);
    }
}
