package com.jobportal.job_portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.job_portal.dto.JobDto;
import com.jobportal.job_portal.service.JobService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
class JobControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;

    @Autowired
    private ObjectMapper objectMapper;

    private JobDto createJobDto(Long jobId, String title) {
        JobDto job = new JobDto();
        job.setId(jobId);
        job.setTitle(title);
        job.setDescription("Sample Job Description");
        return job;
    }

    @Test
    void testGetAllJobs() throws Exception {
        when(jobService.getAllJobs()).thenReturn(Collections.singletonList(createJobDto(1L, "Java Developer")));

        mockMvc.perform(get("/api/jobs/All"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java Developer"));
    }

    @Test
    void testGetJobById() throws Exception {
        when(jobService.getJobById(1L)).thenReturn(createJobDto(1L, "Java Developer"));

        mockMvc.perform(get("/api/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Developer"));
    }

    @Test
    @WithMockUser(username = "recruiter@example.com", roles = {"RECRUITER"})
    void testCreateJob() throws Exception {
        JobDto job = createJobDto(1L, "Java Developer");
        when(jobService.getRecruiterIdFromEmail("recruiter@example.com")).thenReturn(101L);
        when(jobService.createJob(any(JobDto.class), eq(101L))).thenReturn(job);

        mockMvc.perform(post("/api/jobs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(job)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Developer"));
    }

    @Test
    @WithMockUser(username = "recruiter@example.com", roles = {"RECRUITER"})
    void testDeleteJob() throws Exception {
        when(jobService.getRecruiterIdFromEmail("recruiter@example.com")).thenReturn(101L);
        doNothing().when(jobService).deleteJob(1L, 101L);

        mockMvc.perform(delete("/api/jobs/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Job deleted successfully"));
    }
}
