package br.com.backend.builders.entity;

import br.com.backend.entity.Classroom;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.SchoolYear;
import br.com.backend.entity.Student;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

public class EnrollmentBuilder {

    private Student student = StudentBuilder.builder().build();
    private Classroom classroom = ClassroomBuilder.builder().build();
    private SchoolYear schoolYear = SchoolYearBuilder.builder().build();
    private UUID id = UUID.randomUUID();

    public static EnrollmentBuilder builder() {
        return new EnrollmentBuilder();
    }

    public EnrollmentBuilder withStudent(Student student) {
        this.student = student;
        return this;
    }

    public EnrollmentBuilder withSchoolYear(SchoolYear schoolYear) {
        this.schoolYear = schoolYear;
        return this;
    }

    public EnrollmentBuilder withClassroom(Classroom classroom) {
        this.classroom = classroom;
        return this;
    }

    public EnrollmentBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public Enrollment build() {
        Enrollment enrollment =  new Enrollment(student, schoolYear, classroom);
        ReflectionTestUtils.setField(enrollment, "id", UUID.randomUUID());
        return enrollment;
    }
}
