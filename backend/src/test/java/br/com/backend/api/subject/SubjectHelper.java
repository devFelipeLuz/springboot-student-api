package br.com.backend.api.subject;

import br.com.backend.api.authentication.AuthHelper;
import br.com.backend.dto.request.SubjectRequest;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class SubjectHelper {

    @Autowired
    private AuthHelper auth;

    public SubjectData createSubject() {
        String name = "subject " + UUID.randomUUID();

        SubjectRequest request = new SubjectRequest(name);

        String id = given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/subjects")
        .then()
                .statusCode(201)
                .extract()
                .path("id");

        return new SubjectData(UUID.fromString(id), name);
    }
}
