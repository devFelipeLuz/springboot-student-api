package br.com.backend.api.authentication;

import br.com.backend.builders.dto.AuthRequestBuilder;
import br.com.backend.builders.dto.UserCreateRequestBuilder;
import br.com.backend.config.BaseApiTest;
import br.com.backend.dto.request.AuthRequest;
import br.com.backend.dto.request.RefreshRequest;
import br.com.backend.dto.request.UserCreateRequest;
import br.com.backend.entity.enums.Role;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;


public class AuthApiTest extends BaseApiTest {

    @Autowired
    private AuthHelper helper;

    @Test
    void shouldLoginSuccessfully() {
        AuthRequest request = AuthRequestBuilder.builder()
                .withEmail("admin@admin.com")
                .withPassword("admin")
                .build();

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/auth/login")
        .then()
            .statusCode(200)
            .body("accessToken", notNullValue())
            .body("refreshToken", notNullValue());
    }

    @Test
    void shouldAccessProtectedEndpoint() {
        String token = helper.getAdminAccessToken();

        given()
                .header("Authorization", "Bearer " + token)
        .when()
                .get("/students")
        .then()
                .statusCode(200);
    }

    @Test
    void shouldNotAccessStudentWithoutToken() {
        given()
        .when()
                .get("/students")
        .then()
                .statusCode(403);
    }

    @Test
    void shouldNotAccessWithInvalidToken() {
        given()
                .header("Authorization", "Bearer token-invalido")
        .when()
                .get("/students")
        .then()
                .statusCode(401);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        String token = helper.getAdminAccessToken();

        UserCreateRequest request =  UserCreateRequestBuilder.builder()
                .withEmail("test@test.com")
                .withPassword("123456")
                .withRole(Role.STUDENT)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .header("Authorization", "Bearer " + token)
        .when()
                .post("/admin/users")
        .then()
                .statusCode(201);
    }

    @Test
    void shouldReturnForbiddenWhenUserHasNoPermission() {
        String token = helper.getStudentAccessToken();

        UserCreateRequest request = UserCreateRequestBuilder.builder()
                .withEmail("marquinhos@school.com")
                .withPassword("marquinhos")
                .withRole(Role.STUDENT)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .header("Authorization", "Bearer " + token)
        .when()
                .post("/admin/users")
        .then()
                .log().all()
                .statusCode(403);
    }

    @Test
    void shouldLogoutSuccessfully() {
        String token = helper.getAdminAccessToken();

        RefreshRequest request =
                new RefreshRequest(helper.getAdminRefreshToken());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/auth/logout")
        .then()
                .statusCode(204);
    }

    @Test
    void shouldRefreshToken() {
        RefreshRequest request =
                new RefreshRequest(helper.getAdminRefreshToken());

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/auth/refresh")
        .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }
}
