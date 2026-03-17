package br.com.backend;

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

public class StudentControllerIT extends AbstractInegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String defaultStudentJson() {
        return """
                {
                    "name": "Ricardo Cruz",
                    "email": "ricardo.cruz@email.com",
                    "password": "teste@12345"
                }
                """;
    }

    private String createStudentAndReturnId() throws Exception {
        String json = defaultStudentJson();

        MvcResult result = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        return JsonPath.read(response, "$.id");
    }

    @Test
    void shouldCreateStudent() throws Exception {
        String json = defaultStudentJson();

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Ricardo Cruz"))
                .andExpect(jsonPath("$.email").value("ricardo.cruz@email.com"));
    }

    @Test
    void shouldFindStudentById() throws Exception {
        String id = createStudentAndReturnId();

        mockMvc.perform(get("/students/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ricardo Cruz"));

    }

    @Test
    void shouldUpdateStudent() throws Exception {
        String id = createStudentAndReturnId();

        String json = """
                {
                    "name": "Ezio Auditore"
                }
                """;

        mockMvc.perform(patch("/students/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ezio Auditore"))
                .andExpect(jsonPath("$.email").value("ricardo.cruz@email.com"));

        mockMvc.perform(get("/students/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ezio Auditore"));
    }

    @Test
    void shouldDeactivateStudent() throws Exception {
        String id = createStudentAndReturnId();

        mockMvc.perform(delete("/students/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/students/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenStudentNotFound() throws Exception {
        String randomId = UUID.randomUUID().toString();

        mockMvc.perform(get("/students/" + randomId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    void shouldNotCreateStudentWithInvalidEmail() throws Exception {
        String json = """
                {
                    "name": "Student Test",
                    "email": "test",
                    "password": "test"
                }
                """;

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}
