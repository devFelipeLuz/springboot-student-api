package br.com.backend.integration;

import br.com.backend.builders.dto.StudentCreateRequestBuilder;
import br.com.backend.builders.dto.StudentUpdateRequestBuilder;
import br.com.backend.dto.request.StudentCreateRequest;
import br.com.backend.dto.request.StudentUpdateRequest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StudentControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IntegrationTestHelper helper;


    private UUID createStudentAndReturnId() throws Exception {
        StudentCreateRequest request = StudentCreateRequestBuilder.builder()
                .withName("Ricardo Cruz")
                .withEmail("ricardo.cruz@email.com")
                .withPassword("password")
                .build();

        MvcResult result = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(helper.toJson(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        return UUID.fromString(id);
    }

    @Test
    void shouldCreateStudent() throws Exception {
        StudentCreateRequest request = StudentCreateRequestBuilder.builder()
                .withName("Ricardo Cruz")
                .withEmail("ricardo.cruz@email.com")
                .withPassword("password")
                .build();

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(helper.toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Ricardo Cruz"))
                .andExpect(jsonPath("$.email").value("ricardo.cruz@email.com"));
    }

    @Test
    void shouldFindStudentById() throws Exception {
        UUID id = createStudentAndReturnId();

        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ricardo Cruz"));
    }

    @Test
    void shouldUpdateStudent() throws Exception {
        UUID id = createStudentAndReturnId();

        StudentUpdateRequest request = StudentUpdateRequestBuilder.builder()
                .withName("Ezio Auditore")
                .build();

        mockMvc.perform(patch("/students/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(helper.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ezio Auditore"))
                .andExpect(jsonPath("$.email").value("ricardo.cruz@email.com"));

        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ezio Auditore"));
    }

    @Test
    void shouldDeactivateStudent() throws Exception {
        UUID id = createStudentAndReturnId();

        mockMvc.perform(delete("/students/{id}/deactivate", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ricardo Cruz"))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void shouldReturn404WhenStudentNotFound() throws Exception {
        String randomId = UUID.randomUUID().toString();

        mockMvc.perform(get("/students/{id}", randomId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotCreateStudentWithInvalidEmail() throws Exception {
        StudentCreateRequest request = StudentCreateRequestBuilder.builder()
                .withName("Ricardo Cruz")
                .withEmail("wrong")
                .withPassword("password")
                .build();

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(helper.toJson(request)))
                .andExpect(status().isUnprocessableContent());
    }
}
