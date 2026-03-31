package br.com.backend.api.enrollment;

import br.com.backend.api.authentication.AuthHelper;
import br.com.backend.api.classroom.ClassroomData;
import br.com.backend.api.classroom.ClassroomHelper;
import br.com.backend.api.schoolyear.SchoolYearData;
import br.com.backend.api.schoolyear.SchoolYearHelper;
import br.com.backend.api.student.StudentData;
import br.com.backend.api.student.StudentHelper;
import br.com.backend.builders.dto.EnrollmentRequestBuilder;
import br.com.backend.config.BaseApiTest;
import br.com.backend.dto.request.EnrollmentRequest;
import br.com.backend.entity.enums.EnrollmentStatus;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class EnrollmentApiTest extends BaseApiTest {

    @Autowired
    private AuthHelper auth;

    @Autowired
    private StudentHelper studentHelper;

    @Autowired
    private SchoolYearHelper schoolYearHelper;

    @Autowired
    private ClassroomHelper classroomHelper;

    @Autowired
    private EnrollmentHelper helper;

    private StudentData student;
    private SchoolYearData schoolYear;
    private ClassroomData classroom;

    @BeforeEach
    public void setup() {
        student = studentHelper.createStudent();
        schoolYear = schoolYearHelper.createSchoolYear();
        classroom = classroomHelper.createClassroom(schoolYear.getId());
    }

    @Test
    void shouldAllowAdminToCreateEnrollment() {
        EnrollmentRequest request = EnrollmentRequestBuilder.builder()
                .withStudentId(student.getId())
                .withSchoolYearId(schoolYear.getId())
                .withClassroomId(classroom.getId())
                .build();

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/enrollments")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("studentName", equalTo(student.getName()))
                .body("schoolYearName", equalTo(schoolYear.getYear()))
                .body("classroomName", equalTo(classroom.getName()))
                .body("status", equalTo(EnrollmentStatus.ACTIVE.name()));
    }

    @Test
    void shouldReturnForbiddenWhenProfessorCreatesEnrollment() {
        EnrollmentRequest request = EnrollmentRequestBuilder.builder()
                .withStudentId(student.getId())
                .withSchoolYearId(schoolYear.getId())
                .withClassroomId(classroom.getId())
                .build();

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/enrollments")
                .then()
                .statusCode(403);
    }

    @Test
    void shouldReturnForbiddenWhenStudentCreatesEnrollment() {
        EnrollmentRequest request = EnrollmentRequestBuilder.builder()
                .withStudentId(student.getId())
                .withSchoolYearId(schoolYear.getId())
                .withClassroomId(classroom.getId())
                .build();

        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/enrollments")
                .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToGetEnrollmentById() {
        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
        .when()
                .get("/enrollments/{id}", enrollment.getId())
        .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("studentName", equalTo(enrollment.getStudentName()))
                .body("schoolYearName", equalTo(enrollment.getSchoolYear()))
                .body("classroomName", equalTo(enrollment.getClassroomName()))
                .body("status", equalTo(EnrollmentStatus.ACTIVE.name()));
    }

    @Test
    void shouldAllowProfessorToGetEnrollmentById() {
        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
        .when()
                .get("/enrollments/{id}", enrollment.getId())
        .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("studentName", equalTo(enrollment.getStudentName()))
                .body("schoolYearName", equalTo(enrollment.getSchoolYear()))
                .body("classroomName", equalTo(enrollment.getClassroomName()))
                .body("status", equalTo(EnrollmentStatus.ACTIVE.name()));
    }

    @Test
    void shouldReturnForbiddenWhenStudentGetsEnrollmentById() {
        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
        .when()
                .get("/enrollments/{id}", enrollment.getId())
        .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToListEnrollments() {
        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
        .when()
                .get("/enrollments")
        .then()
                .statusCode(200)
                .body("content", not(empty()))
                .body("content.studentName", hasItem(enrollment.getStudentName()));
    }

    @Test
    void shouldAllowProfessorToListEnrollments() {
        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
        .when()
                .get("/enrollments")
        .then()
                .statusCode(200)
                .body("content", not(empty()))
                .body("content.studentName", hasItem(enrollment.getStudentName()));
    }

    @Test
    void shouldFilterEnrollmentsByStudentName() {
        StudentData student = studentHelper.createStudentWithData(
                "Guilherme Briggs", "guilherme.briggs@school.com");
        StudentData anotherStudent = studentHelper.createStudentWithData(
                "Manolo Rei", "manolo.rei@school.com");

        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);
        EnrollmentData anotherEnrollment =
                helper.createEnrollment(anotherStudent, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .queryParam("studentName", "Guilherme")
        .when()
                .get("/enrollments")
        .then()
                .statusCode(200)
                .body("content.studentName", hasItem(enrollment.getStudentName()))
                .body("content.studentName", not(hasItem(anotherEnrollment.getStudentName())))
                .body("content.size()", greaterThanOrEqualTo(1));
    }

    @Test
    void shouldFilterEnrollmentsByStatus() {
        StudentData student = studentHelper.createStudentWithData(
                "Wendel Bezerra", "wendel.bezerra@school.com");
        StudentData anotherStudent = studentHelper.createStudentWithData(
                "Renato Russo", "renato.russo@school.com");

        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);
        EnrollmentData anotherEnrollment =
                helper.createEnrollment(anotherStudent, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
        .when()
                .patch("/enrollments/{id}/cancel", anotherEnrollment.getId())
        .then()
                .statusCode(200);

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .queryParam("status", EnrollmentStatus.ACTIVE.name())
        .when()
                .get("/enrollments")
        .then()
                .statusCode(200)
                .body("content.studentName", hasItem(enrollment.getStudentName()))
                .body("content.studentName", not(hasItem(anotherEnrollment.getStudentName())))
                .body("content.size()", greaterThanOrEqualTo(1));
    }

    @Test
    void shouldFilterEnrollmentsByStudentNameAndStatus() {
        StudentData student = studentHelper.createStudentWithData(
                "Gilberto Barolli", "gilberto.barolli@school.com");
        StudentData anotherStudent = studentHelper.createStudentWithData(
                "Raphael Rossatto", "raphael.rossatto@school.com");

        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);
        EnrollmentData anotherEnrollment =
                helper.createEnrollment(anotherStudent, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
        .when()
                .patch("/enrollments/{id}/cancel", anotherEnrollment.getId())
        .then()
                .statusCode(200);

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .queryParam("studentName", "Gilberto")
                .queryParam("status", EnrollmentStatus.ACTIVE.name())
        .when()
                .get("/enrollments")
        .then()
                .statusCode(200)
                .body("content.studentName", hasItem(enrollment.getStudentName()))
                .body("content.studentName", not(hasItem(anotherEnrollment.getStudentName())))
                .body("content.size()", greaterThanOrEqualTo(1));
    }

    @Test
    void shouldReturnForbiddenWhenStudentListsEnrollments() {
        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
        .when()
                .get("/enrollments")
        .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToFinishEnrollment() {
        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
        .when()
                .patch("/enrollments/{id}/finish", enrollment.getId())
        .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("status", equalTo(EnrollmentStatus.FINISHED.name()));
    }

    @Test
    void shouldReturnForbiddenWhenProfessorFinishesEnrollment() {
        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
        .when()
                .patch("/enrollments/{id}/finish", enrollment.getId())
        .then()
                .statusCode(403);
    }

    @Test
    void shouldReturnForbiddenWhenStudentFinishesEnrollment() {
        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
        .when()
                .patch("/enrollments/{id}/finish", enrollment.getId())
        .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToCancelEnrollment() {
        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
        .when()
                .patch("/enrollments/{id}/cancel", enrollment.getId())
        .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("status", equalTo(EnrollmentStatus.CANCELED.name()));
    }

    @Test
    void shouldReturnForbiddenWhenProfessorCancelsEnrollment() {
        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
        .when()
                .patch("/enrollments/{id}/cancel", enrollment.getId())
        .then()
                .statusCode(403);
    }

    @Test
    void shouldReturnForbiddenWhenStudentCancelsEnrollment() {
        EnrollmentData enrollment =
                helper.createEnrollment(student, schoolYear, classroom);

        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
        .when()
                .patch("/enrollments/{id}/cancel", enrollment.getId())
        .then()
                .statusCode(403);
    }
}
