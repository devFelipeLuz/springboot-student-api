package br.com.backend.api.assignment;

import br.com.backend.api.authentication.AuthHelper;
import br.com.backend.api.classroom.ClassroomData;
import br.com.backend.api.classroom.ClassroomHelper;
import br.com.backend.api.professor.ProfessorData;
import br.com.backend.api.professor.ProfessorHelper;
import br.com.backend.api.schoolyear.SchoolYearData;
import br.com.backend.api.schoolyear.SchoolYearHelper;
import br.com.backend.api.subject.SubjectData;
import br.com.backend.api.subject.SubjectHelper;
import br.com.backend.builders.dto.TeachingAssignmentRequestBuilder;
import br.com.backend.dto.request.TeachingAssignmentRequest;
import io.restassured.http.ContentType;
import org.springframework.boot.test.context.TestComponent;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@TestComponent
public class AssignmentHelper {

    private final AuthHelper auth;
    private final ProfessorHelper professorHelper;
    private final SubjectHelper subjectHelper;
    private final SchoolYearHelper schoolYearHelper;
    private final ClassroomHelper classroomHelper;

    public AssignmentHelper(
            AuthHelper auth,
            ProfessorHelper professorHelper,
            SubjectHelper subjectHelper,
            SchoolYearHelper schoolYearHelper,
            ClassroomHelper classroomHelper) {

        this.auth = auth;
        this.professorHelper = professorHelper;
        this.subjectHelper = subjectHelper;
        this.schoolYearHelper = schoolYearHelper;
        this.classroomHelper = classroomHelper;
    }

    public AssignmentData createAssignment() {
        ProfessorData professor = professorHelper.createProfessor();
        SubjectData subject = subjectHelper.createSubject();
        SchoolYearData schoolYear = schoolYearHelper.createSchoolYear();
        ClassroomData classroom = classroomHelper.createClassroom(schoolYear.getId());

        TeachingAssignmentRequest request = TeachingAssignmentRequestBuilder.builder()
                .professorId(professor.getId())
                .subjectId(subject.getId())
                .classroomId(classroom.getId())
                .build();

        String id = given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/assignments")
        .then()
                .statusCode(201)
                .extract()
                .path("id");

        return new AssignmentData(
                UUID.fromString(id),
                professor.getName(),
                subject.getName(),
                classroom.getName());
    }
}
