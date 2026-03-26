package br.com.backend.api.classroom;

import br.com.backend.api.authentication.AuthHelper;
import br.com.backend.dto.request.ClassroomCreateRequest;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class ClassroomHelper {

    @Autowired
    private AuthHelper auth;

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
}
