package com.jobportal.job_portal.controller;

import com.jobportal.job_portal.entity.JobApplication;
import com.jobportal.job_portal.entity.User;
import com.jobportal.job_portal.repository.UserRepository;
import com.jobportal.job_portal.service.JobApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
class JobApplicationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobApplicationService jobApplicationService;

    @MockitoBean
    private UserRepository userRepository;

    private User createUser(Long id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        return user;
    }

    @Test
    @WithMockUser(username = "applicant@example.com", roles = {"APPLICANT"})
    void testApplyForJob() throws Exception {
        when(userRepository.findByEmail("applicant@example.com"))
                .thenReturn(Optional.of(createUser(100L, "applicant@example.com")));
        when(jobApplicationService.applyForJob(1L, 100L, "resume.pdf", "cover letter"))
                .thenReturn(new JobApplication());

        mockMvc.perform(post("/applications/apply")
                        .param("jobId", "1")
                        .param("resumeUrl", "resume.pdf")
                        .param("coverLetter", "cover letter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(jobApplicationService, times(1))
                .applyForJob(1L, 100L, "resume.pdf", "cover letter");
    }

    @Test
    @WithMockUser(username = "recruiter@example.com", roles = {"RECRUITER"})
    void testGetApplicationsByJob() throws Exception {
        when(userRepository.findByEmail("recruiter@example.com"))
                .thenReturn(Optional.of(createUser(200L, "recruiter@example.com")));
        when(jobApplicationService.getApplicationsByJob(1L, 200L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/applications/by-job/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "applicant@example.com", roles = {"APPLICANT"})
    void testGetMyApplications() throws Exception {
        when(jobApplicationService.getApplicationsByApplicant("applicant@example.com"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/applications/my-applications"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "applicant@example.com", roles = {"APPLICANT"})
    void testWithdrawApplication() throws Exception {
        when(userRepository.findByEmail("applicant@example.com"))
                .thenReturn(Optional.of(createUser(100L, "applicant@example.com")));

        mockMvc.perform(delete("/applications/withdraw/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Application withdrawn successfully!"));
    }
}
