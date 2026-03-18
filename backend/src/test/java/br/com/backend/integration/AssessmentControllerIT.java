package br.com.backend.integration;

import br.com.backend.builders.AssessmentCreateRequestBuilder;
import br.com.backend.dto.request.*;
import br.com.backend.entity.enums.AssessmentType;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AssessmentControllerIT extends AbstractInegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private <T> String toJson(T object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    private <T> UUID postAndReturnId(String url, T request) throws Exception {
        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String id = JsonPath.read(
                result.getResponse().getContentAsString(), "$.id");

        return UUID.fromString(id);
    }

    private UUID createProfessorAndReturnId() throws Exception {
        ProfessorCreateRequest request = new ProfessorCreateRequest(
                "Cristina Ferrari",
                "cristina.ferrari@email.com",
                "123456");

        return postAndReturnId("/professors", request);
    }

    private UUID createSubjectAndReturnId() throws Exception {
        SubjectRequest request =  new SubjectRequest("História");
        return postAndReturnId("/subjects", request);
    }

    private UUID createSchoolYearAndReturnId() throws Exception {
        SchoolYearRequest request = new SchoolYearRequest(2026);
        return postAndReturnId("/school-years", request);
    }

    private UUID createClassroomAndReturnId() throws Exception {
        UUID schoolYearId = createSchoolYearAndReturnId();

        ClassroomCreateRequest request = new ClassroomCreateRequest(
                "3.A", schoolYearId);

        return postAndReturnId("/classrooms", request);
    }

    private UUID createAssignmentAndReturnId() throws Exception {
        UUID professorId = createProfessorAndReturnId();
        UUID subjectId = createSubjectAndReturnId();
        UUID classroomId = createClassroomAndReturnId();

        TeachingAssignmentRequest request = new TeachingAssignmentRequest(
                professorId,  subjectId, classroomId);

        return postAndReturnId("/assignments", request);
    }

    private UUID createAssessmentAndReturnId() throws Exception {
        UUID assignmentId = createAssignmentAndReturnId();

        AssessmentCreateRequest request = AssessmentCreateRequestBuilder.builder()
                .withTitle("Prova de História")
                .withType(AssessmentType.PROVA)
                .withAssignmentId(assignmentId)
                .build();

        return  postAndReturnId("/assessments", request);
    }

    @Test
    void shouldCreateAssessment() throws Exception {
        UUID assignmentId = createAssignmentAndReturnId();

        AssessmentCreateRequest request = AssessmentCreateRequestBuilder.builder()
                .withTitle("Prova de História")
                .withType(AssessmentType.PROVA)
                .withAssignmentId(assignmentId)
                .build();

        mockMvc.perform(post("/assessments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Prova de História"))
                .andExpect(jsonPath("$.subject").value("História"))
                .andExpect(jsonPath("$.type").value(AssessmentType.PROVA.name()))
                .andExpect(jsonPath("$.professorName").value("Cristina Ferrari"))
                .andExpect(jsonPath("$.classroom").value("3.A"));
    }

    @Test
    void shouldFindAssessmentById() throws Exception {
        UUID id = createAssessmentAndReturnId();

        mockMvc.perform(get("/assessments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Prova de História"));
    }

    @Test
    void shouldUpdateAssessment() throws Exception{
        UUID id = createAssessmentAndReturnId();

        AssessmentUpdateRequest request = new AssessmentUpdateRequest(
                "Trabalho de História", AssessmentType.TRABALHO);

        mockMvc.perform(patch("/assessments/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Trabalho de História"))
                .andExpect(jsonPath("$.type").value(AssessmentType.TRABALHO.name()))
                .andExpect(jsonPath("$.professorName").value("Cristina Ferrari"));

        mockMvc.perform(get("/assessments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Trabalho de História"));
    }

    @Test
    void shouldDeleteAssessment() throws Exception {
        UUID id = createAssessmentAndReturnId();

        mockMvc.perform(delete("/assessments/{id}", id))
                .andExpect(status().isNoContent());
    }
}
