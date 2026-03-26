package br.com.backend.api.enrollment;

import br.com.backend.api.authentication.AuthHelper;
import br.com.backend.api.classroom.ClassroomData;
import br.com.backend.api.schoolyear.SchoolYearData;
import br.com.backend.api.student.StudentData;
import br.com.backend.builders.dto.EnrollmentRequestBuilder;
import br.com.backend.dto.request.EnrollmentRequest;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@TestComponent
public class EnrollmentHelper {

    @Autowired
    private AuthHelper auth;

    public EnrollmentData createEnrollment(StudentData student, SchoolYearData schoolYear, ClassroomData classroom) {
        EnrollmentRequest request = EnrollmentRequestBuilder.builder()
                .withStudentId(student.getId())
                .withSchoolYearId(schoolYear.getId())
                .withClassroomId(classroom.getId())
                .build();

        String id = given()
                .header("Authorization", "Bearer " + auth.getAdminAccessToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/enrollments")
        .then()
                .statusCode(201)
                .extract()
                .path("id");

        return new EnrollmentData(
                UUID.fromString(id),
                student.getName(),
                student.getEmail(),
                schoolYear.getYear(),
                classroom.getName());
    }
}
