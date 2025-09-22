package com.jobportal.job_portal.controller;

import com.jobportal.job_portal.entity.JobApplication;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
class SearchApplicationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobApplicationService jobApplicationService;

    @Test
    @WithMockUser(username = "testuser", roles = {"APPLICANT"})
    void testSearchApplicationsByApplicantName() throws Exception {
        JobApplication jobApplication = new JobApplication();
        jobApplication.setId(1L);

        when(jobApplicationService.searchApplicationsByApplicantName("John"))
                .thenReturn(Collections.singletonList(jobApplication));

        mockMvc.perform(get("/api/job-applications")
                        .param("applicantName", "John")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}
