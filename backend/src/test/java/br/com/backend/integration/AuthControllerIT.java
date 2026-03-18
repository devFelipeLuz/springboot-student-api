package br.com.backend.integration;

import br.com.backend.builders.UserBuilder;
import br.com.backend.dto.request.*;
import br.com.backend.entity.User;
import br.com.backend.entity.enums.Role;
import br.com.backend.repository.PasswordResetTokenRepository;
import br.com.backend.repository.RefreshTokenRepository;
import br.com.backend.repository.UserRepository;
import br.com.backend.service.FakeEmailService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(roles = "ADMIN")
public class AuthControllerIT extends AbstractInegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordResetTokenRepository repository;

    @Autowired
    private FakeEmailService  fakeEmailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RefreshTokenRepository  refreshTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    private String email;
    private String password;
    private User user;

    private <T> String toJson(T object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    private MvcResult login() throws Exception{
        AuthRequest request = new AuthRequest(
                email,  password);

        return mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @BeforeEach
    public void createUser() throws  Exception{
        email = "admin@email.com";
        password = "password";

        user = UserBuilder.builder()
                .withEmail(email)
                .withPassword(encoder.encode(password))
                .withRole(Role.ADMIN)
                .build();

        userRepository.save(user);
    }


    @AfterEach
    @Transactional
    public void deleteUser() throws  Exception{
        refreshTokenRepository.deleteAllByUserId(user.getId());
        passwordResetTokenRepository.deleteAllByUserId(user.getId());
        userRepository.deleteById(user.getId());
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        AuthRequest request = new AuthRequest(email, password);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
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
                .content(toJson(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldRequestForgotPassword()  throws Exception {
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);

        mockMvc.perform(post("/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRequestResetPassword()  throws Exception {
        ForgotPasswordRequest request =
                new ForgotPasswordRequest(email);

        mockMvc.perform(post("/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());

        String token = fakeEmailService.getLastToken();

        ResetPasswordRequest resetPasswordRequest =
                new ResetPasswordRequest(token, "superadmin");

        mockMvc.perform(post("/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(resetPasswordRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password successfully reset"));

        AuthRequest newAuthRequest =
                new AuthRequest(email, "superadmin");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(newAuthRequest)))
                .andExpect(status().isOk());
    }
}
