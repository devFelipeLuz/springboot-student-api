package br.com.backend.helper;

import br.com.backend.dto.request.ClassroomCreateRequest;
import br.com.backend.dto.request.EnrollmentRequest;
import br.com.backend.dto.request.SchoolYearRequest;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;

@TestComponent
public class EnrollmentHelper {

    @Autowired
    private AuthHelper auth;

    @Autowired
    private StudentHelper studentHelper;

    public StudentData createStudent() {
        return studentHelper.createStudent();
    }

    public SchoolYearData createSchoolYear() {
        int year = ThreadLocalRandom.current().nextInt(2000, 2100);

        SchoolYearRequest request = new SchoolYearRequest(year);

        String id = given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/school-years")
        .then()
                .statusCode(201)
                .extract()
                .path("id");

        return new SchoolYearData(UUID.fromString(id), year);
    }

    public ClassroomData createClassroom(UUID schoolYearId) {
        String name = "Class " +  UUID.randomUUID();

        ClassroomCreateRequest request = new ClassroomCreateRequest(name, schoolYearId);

        String id = given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/classrooms")
        .then()
                .statusCode(201)
                .extract()
                .path("id");

        return new ClassroomData(UUID.fromString(id), name);
    }

    public EnrollmentData createEnrollment() {
        StudentData student = createStudent();
        SchoolYearData schoolYear = createSchoolYear();
        ClassroomData classroom = createClassroom(schoolYear.getId());

        String id = given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(new EnrollmentRequest(student.getId(), schoolYear.getId(), classroom.getId()))
        .when()
                .post("/enrollments")
        .then()
                .statusCode(201)
                .extract()
                .path("id");

        return new EnrollmentData(
                UUID.fromString(id),
                student.getName(),
                student.getEmail(),
                schoolYear.getYear(),
                classroom.getName());
    }
}
