package br.com.backend.api;

import br.com.backend.builders.dto.EnrollmentRequestBuilder;
import br.com.backend.config.BaseApiTest;
import br.com.backend.dto.request.EnrollmentRequest;
import br.com.backend.entity.enums.EnrollmentStatus;
import br.com.backend.helper.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class EnrollmentApiTest extends BaseApiTest {

    @Autowired
    private AuthHelper auth;

    @Autowired
    private EnrollmentHelper helper;

    @Test
    void shouldAllowAdminToCreateEnrollment() {
        StudentData student = helper.createStudent();
        SchoolYearData schoolYear = helper.createSchoolYear();
        ClassroomData classroom = helper.createClassroom(schoolYear.getId());

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
        StudentData student = helper.createStudent();
        SchoolYearData schoolYear = helper.createSchoolYear();
        ClassroomData classroom = helper.createClassroom(schoolYear.getId());

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
        StudentData student = helper.createStudent();
        SchoolYearData schoolYear = helper.createSchoolYear();
        ClassroomData classroom = helper.createClassroom(schoolYear.getId());

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
        EnrollmentData enrollment = helper.createEnrollment();

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
        EnrollmentData enrollment = helper.createEnrollment();

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
        EnrollmentData enrollment = helper.createEnrollment();

        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
        .when()
                .get("/enrollments/{id}", enrollment.getId())
        .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToListEnrollments() {
        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
        .when()
                .get("/enrollments")
        .then()
                .statusCode(200);
    }

    @Test
    void shouldAllowProfessorToListEnrollments() {
        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
        .when()
                .get("/enrollments")
        .then()
                .statusCode(200);
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
        EnrollmentData enrollment = helper.createEnrollment();

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
        EnrollmentData enrollment = helper.createEnrollment();

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
        .when()
                .patch("/enrollments/{id}/finish", enrollment.getId())
        .then()
                .statusCode(403);
    }

    @Test
    void shouldReturnForbiddenWhenStudentFinishesEnrollment() {
        EnrollmentData enrollment = helper.createEnrollment();

        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
        .when()
                .patch("/enrollments/{id}/finish", enrollment.getId())
        .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToCancelEnrollment() {
        EnrollmentData enrollment = helper.createEnrollment();

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
        EnrollmentData enrollment = helper.createEnrollment();

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
                .when()
                .patch("/enrollments/{id}/cancel", enrollment.getId())
                .then()
                .statusCode(403);
    }

    @Test
    void shouldReturnForbiddenWhenStudentCancelsEnrollment() {
        EnrollmentData enrollment = helper.createEnrollment();

        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
                .when()
                .patch("/enrollments/{id}/cancel", enrollment.getId())
                .then()
                .statusCode(403);
    }
}
