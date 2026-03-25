package br.com.backend.helper;

import br.com.backend.builders.dto.AuthRequestBuilder;
import br.com.backend.dto.request.AuthRequest;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.boot.test.context.TestComponent;

import static io.restassured.RestAssured.given;

@TestComponent
public class AuthHelper {

    private ExtractableResponse<Response> getResponse(String email, String password) {
        AuthRequest request = AuthRequestBuilder.builder()
                .withEmail(email)
                .withPassword(password)
                .build();

        return given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/auth/login")
        .then()
                .statusCode(200)
                .extract();
    }

    public String getAccessToken(String email, String password) {
        return getResponse(email, password).path("accessToken");
    }

    public String getRefreshToken(String email, String password) {
        return getResponse(email, password).path("refreshToken");
    }

    public String getAdminAccessToken() {
        return getAccessToken("admin@admin.com", "admin");
    }

    public String getAdminRefreshToken() {
        return getRefreshToken("admin@admin.com", "admin");
    }

    public String getProfessorAccessToken() {
        return getAccessToken("professor@school.com", "professor");
    }

    public String getProfessorRefreshToken() {
        return getRefreshToken("professor@school.com", "professor");
    }

    public String getStudentAccessToken() {
        return getAccessToken("student@school.com", "student");
    }

    public String getStudentRefreshToken() {
        return getRefreshToken("student@school.com", "student");
    }

}
