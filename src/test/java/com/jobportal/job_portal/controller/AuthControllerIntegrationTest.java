package com.jobportal.job_portal.controller;

import com.jobportal.job_portal.dto.AuthRequest;
import com.jobportal.job_portal.dto.RegisterRequest;
import com.jobportal.job_portal.entity.User;
import com.jobportal.job_portal.repository.JobApplicationRepository;
import com.jobportal.job_portal.repository.JobRepository;
import com.jobportal.job_portal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jobApplicationRepository.deleteAll();
        jobRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testRegisterNewUser() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("John Doe");
        request.setEmail("john@example.com");
        request.setPhone("1234567890");
        request.setPassword("pass123");
        request.setRole("APPLICANT");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void testRegisterWithExistingEmail() throws Exception {

        userRepository.save(User.builder()
                .fullName("Jane Doe")
                .email("jane@example.com")
                .phone("1234567890")
                .password("password")
                .role("APPLICANT")
                .username("jane@example.com")
                .build());

        RegisterRequest request = new RegisterRequest();
        request.setFullName("New User");
        request.setEmail("jane@example.com");
        request.setPhone("9999999999");
        request.setPassword("pass123");
        request.setRole("APPLICANT");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));
    }

    @Test
    void testLoginUser() throws Exception {

        userRepository.save(User.builder()
                .fullName("Test User")
                .email("test@example.com")
                .phone("1234567890")
                .password("pass123")
                .role("APPLICANT")
                .username("test@example.com")
                .build());

        AuthRequest request = new AuthRequest();
        request.setEmail("test@example.com");
        request.setPassword("pass123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists()); // expecting JWT token
    }
}
