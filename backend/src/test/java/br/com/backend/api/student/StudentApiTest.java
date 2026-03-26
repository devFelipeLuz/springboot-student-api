package br.com.backend.api.student;

import br.com.backend.builders.dto.StudentCreateRequestBuilder;
import br.com.backend.builders.dto.StudentUpdateRequestBuilder;
import br.com.backend.config.BaseApiTest;
import br.com.backend.dto.request.StudentCreateRequest;
import br.com.backend.dto.request.StudentUpdateRequest;
import br.com.backend.api.authentication.AuthHelper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class StudentApiTest extends BaseApiTest {

    @Autowired
    private AuthHelper auth;

    @Autowired
    private StudentHelper helper;

    @Test
    void shouldAllowAdminToCreateStudent() {
        StudentCreateRequest request = StudentCreateRequestBuilder.builder()
                .withName("Guilherme Briggs")
                .withEmail("guilherme.briggs@school.com")
                .build();

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/students")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Guilherme Briggs"))
                .body("email", equalTo("guilherme.briggs@school.com"));
    }

    @Test
    void shouldNotAllowNonAdminToCreateStudent() {
        StudentCreateRequest request = StudentCreateRequestBuilder.builder()
                .withName("Aluno " + UUID.randomUUID())
                .withEmail("email." + UUID.randomUUID() + "@school.com")
                .build();

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/students")
        .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToGetStudentById() {
        StudentData student = helper.createStudent();

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
        .when()
                .get("/students/{id}", student.getId())
        .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo(student.getName()))
                .body("email", equalTo(student.getEmail()));
    }

    @Test
    void shouldAllowStudentToGetOwnData() {
        StudentData student = helper.createStudent();

        given()
                .header("Authorization", "Bearer " + auth.getAccessToken(
                        student.getEmail(), student.getPassword()))
        .when()
                .get("/students/{id}", student.getId())
        .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo(student.getName()))
                .body("email", equalTo(student.getEmail()));
    }

    @Test
    void shouldReturnForbiddenWhenStudentAccessesAnotherStudent() {
        StudentData student = helper.createStudent();
        StudentData anotherStudent = helper.createStudent();

        given()
                .header("Authorization", "Bearer " + auth.getAccessToken(
                        student.getEmail(), student.getPassword()))
        .when()
                .get("/students/{id}", anotherStudent.getId())
        .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToListStudents() {
        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
        .when()
                .get("/students")
        .then()
                .statusCode(200);
    }

    @Test
    void shouldAllowProfessorToListStudents() {
        StudentData student = helper.createStudent();

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
        .when()
                .get("/students")
        .then()
                .statusCode(200)
                .body("content", not(empty()))
                .body("content.name", hasItem(student.getName()));
    }

    @Test
    void shouldReturnForbiddenWhenStudentListsStudents() {
        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
        .when()
                .get("/students")
        .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToUpdateStudent() {
        StudentData student = helper.createStudent();

        StudentUpdateRequest request = StudentUpdateRequestBuilder.builder()
                .withName("Aluno " + UUID.randomUUID())
                .withEmail("email." + UUID.randomUUID() + "@school.com")
                .withPassword("password")
                .build();

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .patch("/students/{id}", student.getId())
        .then()
                .statusCode(200)
                .body("name", equalTo(request.name()))
                .body("email", equalTo(request.email()));
    }

    @Test
    void shouldAllowStudentToUpdateOwnData() {
        StudentData student = helper.createStudent();

        StudentUpdateRequest request = StudentUpdateRequestBuilder.builder()
                .withName("Aluno " + UUID.randomUUID())
                .withEmail("email." + UUID.randomUUID() + "@school.com")
                .withPassword("password")
                .build();

        given()
                .header("Authorization", "Bearer " + auth.getAccessToken(
                        student.getEmail(), student.getPassword()))
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .patch("/students/{id}", student.getId())
        .then()
                .statusCode(200)
                .body("name", equalTo(request.name()))
                .body("email", equalTo(request.email()));
    }

    @Test
    void shouldReturnForbiddenWhenNonAdminUpdatesAnotherStudent() {
        StudentData student = helper.createStudent();
        StudentData anotherStudent = helper.createStudent();

        StudentUpdateRequest request = StudentUpdateRequestBuilder.builder()
                .withName("Aluno " + UUID.randomUUID())
                .withEmail("email." + UUID.randomUUID() + "@school.com")
                .withPassword("password")
                .build();

        given()
                .header("Authorization", "Bearer " + auth.getAccessToken(
                        student.getEmail(), student.getPassword()))
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .patch("/students/{id}", anotherStudent.getId())
        .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToDeactivateStudent() {
        StudentData student = helper.createStudent();

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
        .when()
                .delete("/students/{id}/deactivate", student.getId())
        .then()
                .statusCode(204);
    }

    @Test
    void shouldReturnForbiddenWhenNonAdminDeactivatesStudent() {
        StudentData student = helper.createStudent();

        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
        .when()
                .delete("/students/{id}/deactivate", student.getId())
        .then()
                .statusCode(403);
    }
}
