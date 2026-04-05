package br.com.backend.integration;

import br.com.backend.builders.dto.EnrollmentRequestBuilder;
import br.com.backend.dto.request.ClassroomCreateRequest;
import br.com.backend.dto.request.EnrollmentRequest;
import br.com.backend.dto.request.SchoolYearRequest;
import br.com.backend.dto.request.StudentCreateRequest;
import br.com.backend.entity.enums.EnrollmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EnrollmentControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IntegrationTestHelper helper;


    private UUID createStudentAndReturnId() throws Exception {
        StudentCreateRequest request = new StudentCreateRequest(
                "Ricardo Juarez",
                "ricardo.juarez@email.com",
                "123456");

        return helper.postAndReturnId("/students", request);
    }

    private UUID createSchoolYearAndReturnId() throws Exception {
        SchoolYearRequest request = new SchoolYearRequest(2026);

        return helper.postAndReturnId("/school-years", request);
    }

    private UUID createClassroomAndReturnId() throws Exception {
        UUID schoolYearId = createSchoolYearAndReturnId();

        ClassroomCreateRequest request = new ClassroomCreateRequest("3.A", schoolYearId);

        return helper.postAndReturnId("/classrooms", request);
    }

    private UUID createEnrollmentAndReturnId() throws Exception {
        UUID studentId = createStudentAndReturnId();
        UUID schoolYearId = createSchoolYearAndReturnId();
        UUID classroomId = createClassroomAndReturnId();

        EnrollmentRequest request = EnrollmentRequestBuilder.builder()
                .withStudentId(studentId)
                .withSchoolYearId(schoolYearId)
                .withClassroomId(classroomId)
                .build();

        return helper.postAndReturnId("/enrollments", request);
    }

    @Test
    void shouldCreateEnrollment() throws Exception {
        UUID studentId = createStudentAndReturnId();
        UUID schoolYearId = createSchoolYearAndReturnId();
        UUID classroomId = createClassroomAndReturnId();

        EnrollmentRequest request = EnrollmentRequestBuilder.builder()
                .withStudentId(studentId)
                .withSchoolYearId(schoolYearId)
                .withClassroomId(classroomId)
                .build();

        mockMvc.perform(post("/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(helper.toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.studentName").value("Ricardo Juarez"))
                .andExpect(jsonPath("$.schoolYearName").value("2026"))
                .andExpect(jsonPath("$.classroomName").value("3.A"));
    }

    @Test
    void shouldFindEnrollmentById() throws Exception {
        UUID id = createEnrollmentAndReturnId();

        mockMvc.perform(get("/enrollments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.studentName").value("Ricardo Juarez"));
    }

    @Test
    void shouldCancelEnrollment()  throws Exception {
        UUID id = createEnrollmentAndReturnId();

        mockMvc.perform(patch("/enrollments/{id}/cancel", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(EnrollmentStatus.CANCELED.name()));
    }
}
