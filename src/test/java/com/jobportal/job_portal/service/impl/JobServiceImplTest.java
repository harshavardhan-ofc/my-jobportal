package com.jobportal.job_portal.service.impl;

import com.jobportal.job_portal.dto.JobDto;
import com.jobportal.job_portal.entity.Job;
import com.jobportal.job_portal.entity.User;
import com.jobportal.job_portal.repository.JobRepository;
import com.jobportal.job_portal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobServiceImplTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JobServiceImpl jobService;

    private User recruiter;
    private Job job;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        recruiter = User.builder()
                .id(1L)
                .email("recruiter@example.com")
                .role("RECRUITER")
                .build();

        job = Job.builder()
                .id(101L)
                .title("Java Developer")
                .description("Backend Developer")
                .companyName("Tech Corp")
                .location("Remote")
                .employmentType("Full-time")
                .salary(80000.0)
                .postedAt(LocalDateTime.now())
                .postedBy(recruiter)
                .build();
    }

    @Test
    void testCreateJob_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(recruiter));
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        JobDto jobDto = JobDto.builder()
                .title("Java Developer")
                .description("Backend Developer")
                .companyName("Tech Corp")
                .location("Remote")
                .employmentType("Full-time")
                .salary(80000.0)
                .build();

        JobDto result = jobService.createJob(jobDto, 1L);

        assertNotNull(result);
        assertEquals("Java Developer", result.getTitle());
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void testGetRecruiterIdFromEmail_Success() {
        when(userRepository.findByEmail("recruiter@example.com")).thenReturn(Optional.of(recruiter));

        Long recruiterId = jobService.getRecruiterIdFromEmail("recruiter@example.com");

        assertEquals(1L, recruiterId);
    }

    @Test
    void testGetRecruiterIdFromEmail_UserNotFound() {
        when(userRepository.findByEmail("wrong@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jobService.getRecruiterIdFromEmail("wrong@example.com"));

        assertEquals("User not found with email: wrong@example.com", exception.getMessage());
    }

    @Test
    void testGetAllJobs() {
        when(jobRepository.findAll()).thenReturn(Arrays.asList(job));

        List<JobDto> jobs = jobService.getAllJobs();

        assertEquals(1, jobs.size());
        assertEquals("Java Developer", jobs.get(0).getTitle());
    }

    @Test
    void testGetJobById_Success() {
        when(jobRepository.findById(101L)).thenReturn(Optional.of(job));

        JobDto jobDto = jobService.getJobById(101L);

        assertEquals("Java Developer", jobDto.getTitle());
    }

    @Test
    void testDeleteJob_Success() {
        when(jobRepository.findById(101L)).thenReturn(Optional.of(job));

        jobService.deleteJob(101L, 1L);

        verify(jobRepository, times(1)).delete(job);
    }

    @Test
    void testDeleteJob_Unauthorized() {
        User otherRecruiter = User.builder().id(2L).email("other@example.com").role("RECRUITER").build();
        Job otherJob = Job.builder().id(102L).title("Other Job").postedBy(otherRecruiter).build();

        when(jobRepository.findById(102L)).thenReturn(Optional.of(otherJob));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jobService.deleteJob(102L, 1L));

        assertEquals("You are not authorized to delete this job", exception.getMessage());
    }
}
