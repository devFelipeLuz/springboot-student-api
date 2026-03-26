package br.com.backend.integration;

import br.com.backend.config.DataInitializer;
import br.com.backend.dto.request.*;
import br.com.backend.entity.User;
import br.com.backend.repository.UserRepository;
import br.com.backend.service.FakeEmailService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(roles = "ADMIN")
@Import(DataInitializer.class)
public class AuthControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    IntegrationTestHelper helper;

    @Autowired
    private FakeEmailService  fakeEmailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private final String email = "admin@admin.com";
    private final String password = "admin";
    private User user;

    private MvcResult login() throws Exception{
        AuthRequest request = new AuthRequest(
                email,  password);

        return mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(helper.toJson(request)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        AuthRequest request = new AuthRequest(email, password);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(helper.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void shouldLogoutSuccessfully() throws Exception {
        String refreshToken = JsonPath.read(
                login().getResponse().getContentAsString(), "$.refreshToken");

        RefreshRequest request = new RefreshRequest(refreshToken);

        mockMvc.perform(post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(helper.toJson(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldRequestForgotPassword()  throws Exception {
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);

        mockMvc.perform(post("/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(helper.toJson(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRequestResetPassword()  throws Exception {
        ForgotPasswordRequest request =
                new ForgotPasswordRequest(email);

        mockMvc.perform(post("/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(helper.toJson(request)))
                .andExpect(status().isOk());

        String token = fakeEmailService.getLastToken();

        ResetPasswordRequest resetPasswordRequest =
                new ResetPasswordRequest(token, "superadmin");

        mockMvc.perform(post("/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(helper.toJson(resetPasswordRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password successfully reset"));

        AuthRequest newAuthRequest =
                new AuthRequest(email, "superadmin");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(helper.toJson(newAuthRequest)))
                .andExpect(status().isOk());
    }
}
