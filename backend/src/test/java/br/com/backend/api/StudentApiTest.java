package br.com.backend.api;

import br.com.backend.builders.dto.StudentCreateRequestBuilder;
import br.com.backend.builders.dto.StudentUpdateRequestBuilder;
import br.com.backend.config.BaseApiTest;
import br.com.backend.dto.request.StudentCreateRequest;
import br.com.backend.dto.request.StudentUpdateRequest;
import br.com.backend.helper.AuthHelper;
import br.com.backend.helper.StudentData;
import br.com.backend.helper.StudentHelper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

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
        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
        .when()
                .get("/students")
        .then()
                .statusCode(200);
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
        String name = "Aluno " + UUID.randomUUID();
        String email = "email." + UUID.randomUUID() + "@school.com";
        String password = "password";

        StudentUpdateRequest request = StudentUpdateRequestBuilder.builder()
                .withName(name)
                .withEmail(email)
                .withPassword(password)
                .build();

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .patch("/students/{id}", student.getId())
        .then()
                .statusCode(200)
                .body("name", equalTo(name))
                .body("email", equalTo(email));
    }

    @Test
    void shouldAllowStudentToUpdateOwnData() {
        StudentData student = helper.createStudent();
        String name = "Aluno " + UUID.randomUUID();
        String email = "email." + UUID.randomUUID() + "@school.com";
        String password = "password";

        StudentUpdateRequest request = StudentUpdateRequestBuilder.builder()
                .withName(name)
                .withEmail(email)
                .withPassword(password)
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
                .body("name", equalTo(name))
                .body("email", equalTo(email));
    }

    @Test
    void shouldReturnForbiddenWhenNonAdminUpdatesAnotherStudent() {
        StudentData student = helper.createStudent();
        StudentData anotherStudent = helper.createStudent();

        String name = "Aluno " + UUID.randomUUID();
        String email = "email." + UUID.randomUUID() + "@school.com";
        String password = "password";

        StudentUpdateRequest request = StudentUpdateRequestBuilder.builder()
                .withName(name)
                .withEmail(email)
                .withPassword(password)
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
