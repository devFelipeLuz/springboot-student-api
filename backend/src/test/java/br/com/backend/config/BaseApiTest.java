package br.com.backend.config;

import br.com.backend.helper.AuthHelper;
import br.com.backend.helper.EnrollmentHelper;
import br.com.backend.helper.StudentHelper;
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
@Import({DataInitializer.class, AuthHelper.class, StudentHelper.class, EnrollmentHelper.class})
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
