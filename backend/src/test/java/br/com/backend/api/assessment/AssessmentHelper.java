package br.com.backend.api.assessment;

import br.com.backend.api.authentication.AuthHelper;
import br.com.backend.builders.dto.AssessmentCreateRequestBuilder;
import br.com.backend.dto.request.AssessmentCreateRequest;
import br.com.backend.entity.enums.AssessmentType;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@TestComponent
public class AssessmentHelper {

    @Autowired
    private AuthHelper auth;

    public AssessmentData createAssessment(UUID assignmentId) {
        String title = "Title " + UUID.randomUUID();

        AssessmentCreateRequest request = AssessmentCreateRequestBuilder.builder()
                .withTitle(title)
                .withType(AssessmentType.PROVA)
                .withAssignmentId(assignmentId)
                .build();

        String id = given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/assessments")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        return new AssessmentData(
                UUID.fromString(id),
                request.title(),
                request.type().name(),
                request.teachingAssignmentId());
    }
}
