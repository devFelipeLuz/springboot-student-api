package br.com.backend.config;

import br.com.backend.api.assessment.AssessmentHelper;
import br.com.backend.api.assignment.AssignmentHelper;
import br.com.backend.api.authentication.AuthHelper;
import br.com.backend.api.classroom.ClassroomHelper;
import br.com.backend.api.enrollment.EnrollmentHelper;
import br.com.backend.api.professor.ProfessorHelper;
import br.com.backend.api.schoolyear.SchoolYearHelper;
import br.com.backend.api.student.StudentHelper;
import br.com.backend.api.subject.SubjectHelper;
import br.com.backend.integration.AbstractIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Import({DataInitializer.class,
        AuthHelper.class,
        StudentHelper.class,
        EnrollmentHelper.class,
        AssignmentHelper.class,
        AssessmentHelper.class,
        ClassroomHelper.class,
        ProfessorHelper.class,
        SchoolYearHelper.class,
        SubjectHelper.class})
public abstract class BaseApiTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setupRestAssured() {
        RestAssured.reset();

        RestAssured.baseURI = "http://127.0.0.1";
        RestAssured.port = port;

        RestAssured.config =  RestAssuredConfig.config()
                .encoderConfig(EncoderConfig.encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false));
    }
}
