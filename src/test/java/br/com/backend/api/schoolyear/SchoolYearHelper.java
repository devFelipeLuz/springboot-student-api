package br.com.backend.api.schoolyear;

import br.com.backend.api.authentication.AuthHelper;
import br.com.backend.dto.request.SchoolYearRequest;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;

public class SchoolYearHelper {

    @Autowired
    private AuthHelper auth;

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
}
