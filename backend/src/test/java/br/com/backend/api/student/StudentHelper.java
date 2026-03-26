package br.com.backend.api.student;

import br.com.backend.builders.dto.StudentCreateRequestBuilder;
import br.com.backend.dto.request.StudentCreateRequest;
import br.com.backend.api.authentication.AuthHelper;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@TestComponent
public class StudentHelper {

    @Autowired
    private AuthHelper auth;

    public StudentData createStudent() {
        String name = "Aluno " + UUID.randomUUID();
        String email = "email." + UUID.randomUUID() + "@school.com";
        String password = "student";

        StudentCreateRequest request = StudentCreateRequestBuilder.builder()
                .withName(name)
                .withEmail(email)
                .withPassword(password)
                .build();

        String id = given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/students")
        .then()
                .statusCode(201)
                .extract()
                .path("id");

        return new StudentData(UUID.fromString(id), name, email, password);
    }
}
