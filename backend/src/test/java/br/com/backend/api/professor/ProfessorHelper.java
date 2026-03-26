package br.com.backend.api.professor;

import br.com.backend.api.authentication.AuthHelper;
import br.com.backend.dto.request.ProfessorCreateRequest;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class ProfessorHelper {

    @Autowired
    private AuthHelper auth;

    public ProfessorData createProfessor() {
        String name = "professor " + UUID.randomUUID();
        String email = "email." + UUID.randomUUID() + "@school.com";
        String password = "professor";

        ProfessorCreateRequest request =
                new ProfessorCreateRequest(name, email, password);

        String id = given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/professors")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        return new ProfessorData(UUID.fromString(id), name, email, password);
    }
}
