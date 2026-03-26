package br.com.backend.api.assessment;

import br.com.backend.api.assignment.AssignmentHelper;
import br.com.backend.builders.dto.AssessmentCreateRequestBuilder;
import br.com.backend.config.BaseApiTest;
import br.com.backend.dto.request.AssessmentCreateRequest;
import br.com.backend.dto.request.AssessmentUpdateRequest;
import br.com.backend.entity.enums.AssessmentType;
import br.com.backend.api.assignment.AssignmentData;
import br.com.backend.api.authentication.AuthHelper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AssessmentApiTest extends BaseApiTest {

    @Autowired
    private AuthHelper auth;

    @Autowired
    private AssignmentHelper assignmentHelper;

    @Autowired
    private AssessmentHelper helper;

    private AssignmentData assignment;

    @BeforeEach
    public void setup() {
        assignment = assignmentHelper.createAssignment();
    }

    @Test
    void shouldAllowAdminToCreateAssessment() {
        AssessmentCreateRequest request = AssessmentCreateRequestBuilder.builder()
                .withTitle("Title " + UUID.randomUUID())
                .withType(AssessmentType.PROVA)
                .withAssignmentId(assignment.getId())
                .build();

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/assessments")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo(request.title()))
                .body("subject", equalTo(assignment.getSubjectName()))
                .body("type", equalTo(request.type().name()))
                .body("professorName", equalTo(assignment.getProfessorName()))
                .body("classroom", equalTo(assignment.getClassroomName()))
                .body("date", notNullValue());
    }

    @Test
    void shouldAllowProfessorToCreateAssessment() {
        AssessmentCreateRequest request = AssessmentCreateRequestBuilder.builder()
                .withTitle("Title " + UUID.randomUUID())
                .withType(AssessmentType.PROVA)
                .withAssignmentId(assignment.getId())
                .build();

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/assessments")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo(request.title()))
                .body("subject", equalTo(assignment.getSubjectName()))
                .body("type", equalTo(request.type().name()))
                .body("professorName", equalTo(assignment.getProfessorName()))
                .body("classroom", equalTo(assignment.getClassroomName()))
                .body("date", notNullValue());
    }

    @Test
    void shouldReturnForbiddenWhenStudentCreatesAssessment() {
        AssessmentCreateRequest request = AssessmentCreateRequestBuilder.builder()
                .withTitle("Title " + UUID.randomUUID())
                .withType(AssessmentType.PROVA)
                .withAssignmentId(assignment.getId())
                .build();

        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/assessments")
        .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToGetAssessmentById() {
        AssessmentData assessment = helper.createAssessment(assignment.getId());

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
        .when()
                .get("/assessments/{id}", assessment.getId())
        .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("title", equalTo(assessment.getTitle()))
                .body("subject", equalTo(assignment.getSubjectName()))
                .body("type", equalTo(assessment.getType()))
                .body("professorName", equalTo(assignment.getProfessorName()))
                .body("classroom", equalTo(assignment.getClassroomName()))
                .body("date", notNullValue());
    }

    @Test
    void shouldAllowProfessorToGetAssessmentById() {
        AssessmentData assessment = helper.createAssessment(assignment.getId());

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
        .when()
                .get("/assessments/{id}", assessment.getId())
        .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("title", equalTo(assessment.getTitle()))
                .body("subject", equalTo(assignment.getSubjectName()))
                .body("type", equalTo(assessment.getType()))
                .body("professorName", equalTo(assignment.getProfessorName()))
                .body("classroom", equalTo(assignment.getClassroomName()))
                .body("date", notNullValue());
    }

    @Test
    void shouldReturnForbiddenWhenStudentGetsAssessmentById() {
        AssessmentData assessment = helper.createAssessment(assignment.getId());

        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
        .when()
                .get("/assessments/{id}", assessment.getId())
        .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToListAssessments() {
        AssessmentData assessment = helper.createAssessment(assignment.getId());

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
        .when()
                .get("/assessments")
        .then()
                .statusCode(200)
                .body("content", not(empty()))
                .body("content.title", hasItem(assessment.getTitle()));
    }

    @Test
    void shouldAllowProfessorToListAssessments() {
        AssessmentData assessment = helper.createAssessment(assignment.getId());

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
        .when()
                .get("/assessments")
        .then()
                .statusCode(200)
                .body("content", not(empty()))
                .body("content.title", hasItem(assessment.getTitle()));
    }

    @Test
    void shouldReturnForbiddenWhenStudentListsAssessments() {
        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
        .when()
                .get("/assessments")
        .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToUpdateAssessment() {
        AssessmentData assessment = helper.createAssessment(assignment.getId());

        AssessmentUpdateRequest request =
                new AssessmentUpdateRequest("Title " + UUID.randomUUID(), AssessmentType.TRABALHO);

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .patch("/assessments/{id}", assessment.getId())
        .then()
                .statusCode(200)
                .body("title", equalTo(request.title()))
                .body("type", equalTo(AssessmentType.TRABALHO.name()));
    }

    @Test
    void shouldAllowProfessorToUpdateAssessment() {
        AssessmentData assessment = helper.createAssessment(assignment.getId());

        AssessmentUpdateRequest request =
                new AssessmentUpdateRequest("Title " + UUID.randomUUID(), AssessmentType.TRABALHO);

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .patch("/assessments/{id}", assessment.getId())
        .then()
                .statusCode(200)
                .body("title", equalTo(request.title()))
                .body("type", equalTo(AssessmentType.TRABALHO.name()));
    }

    @Test
    void shouldReturnForbiddenWhenStudentUpdatesAssessment() {
        AssessmentData assessment = helper.createAssessment(assignment.getId());

        AssessmentUpdateRequest request =
                new AssessmentUpdateRequest("Title " + UUID.randomUUID(), AssessmentType.TRABALHO);

        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .patch("/assessments/{id}", assessment.getId())
        .then()
                .statusCode(403);
    }

    @Test
    void shouldAllowAdminToDeleteAssessment() {
        AssessmentData assessment = helper.createAssessment(assignment.getId());

        given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
        .when()
                .delete("/assessments/{id}", assessment.getId())
        .then()
                .statusCode(204);
    }

    @Test
    void shouldAllowProfessorToDeleteAssessment() {
        AssessmentData assessment = helper.createAssessment(assignment.getId());

        given()
                .header("Authorization", "Bearer " + auth.getProfessorAccessToken())
        .when()
                .delete("/assessments/{id}", assessment.getId())
        .then()
                .statusCode(204);
    }

    @Test
    void shouldReturnForbiddenWhenStudentDeletesAssessment() {
        AssessmentData assessment = helper.createAssessment(assignment.getId());

        given()
                .header("Authorization", "Bearer " + auth.getStudentAccessToken())
        .when()
                .delete("/assessments/{id}", assessment.getId())
        .then()
                .statusCode(403);
    }
}
