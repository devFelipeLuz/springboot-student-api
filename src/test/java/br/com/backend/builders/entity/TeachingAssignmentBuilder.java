package br.com.backend.builders.entity;

import br.com.backend.entity.Classroom;
import br.com.backend.entity.Professor;
import br.com.backend.entity.Subject;
import br.com.backend.entity.TeachingAssignment;

public class TeachingAssignmentBuilder {

    private Professor professor = ProfessorBuilder.builder().build();
    private Subject subject = SubjectBuilder.builder().build();
    private Classroom classroom = ClassroomBuilder.builder().build();

    public static TeachingAssignmentBuilder builder() {
        return new TeachingAssignmentBuilder();
    }

    public TeachingAssignmentBuilder withProfessor(Professor professor) {
        this.professor = professor;
        return this;
    }

    public TeachingAssignmentBuilder withSubject(Subject subject) {
        this.subject = subject;
        return this;
    }

    public TeachingAssignmentBuilder withClassroom(Classroom classroom) {
        this.classroom = classroom;
        return this;
    }

    public TeachingAssignment build() {
        return new TeachingAssignment(professor, subject, classroom);
    }
}
