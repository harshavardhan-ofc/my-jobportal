package com.jobportal.job_portal.service.impl;

import com.jobportal.job_portal.entity.Job;
import com.jobportal.job_portal.entity.JobApplication;
import com.jobportal.job_portal.entity.User;
import com.jobportal.job_portal.repository.JobApplicationRepository;
import com.jobportal.job_portal.repository.JobRepository;
import com.jobportal.job_portal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JobApplicationServiceImplTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JobApplicationServiceImpl jobApplicationService;

    private Job job;
    private User applicant;
    private JobApplication application;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        job = new Job();
        job.setId(1L);

        applicant = new User();
        applicant.setId(2L);
        applicant.setFullName("ranjith kumar");
        applicant.setEmail("ranjitha@gmail.com");
        applicant.setRole("APPLICANT");

        application = JobApplication.builder()
                .id(10L)
                .applicant(applicant)
                .resumeUrl("resume.pdf")
                .coverLetter("cover letter")
                .appliedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testApplyForJob_Success() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(userRepository.findById(2L)).thenReturn(Optional.of(applicant));
        when(jobApplicationRepository.existsByJobIdAndApplicantId(1L, 2L)).thenReturn(false);
        when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(application);

        JobApplication saved = jobApplicationService.applyForJob(1L, 2L, "resume.pdf", "cover letter");

        assertNotNull(saved);
        assertEquals("resume.pdf", saved.getResumeUrl());
        verify(jobApplicationRepository, times(1)).save(any(JobApplication.class));
    }

    @Test
    void testApplyForJob_JobNotFound() {
        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jobApplicationService.applyForJob(1L, 2L, "resume.pdf", "cover letter"));
        assertEquals("Job not found", exception.getMessage());
    }

    @Test
    void testApplyForJob_ApplicantNotFound() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jobApplicationService.applyForJob(1L, 2L, "resume.pdf", "cover letter"));
        assertEquals("Applicant not found", exception.getMessage());
    }

    @Test
    void testApplyForJob_InvalidRole() {
        applicant.setRole("RECRUITER"); // Not allowed
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(userRepository.findById(2L)).thenReturn(Optional.of(applicant));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jobApplicationService.applyForJob(1L, 2L, "resume.pdf", "cover letter"));
        assertEquals("Only applicants can apply for jobs", exception.getMessage());
    }

    @Test
    void testApplyForJob_AlreadyApplied() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(userRepository.findById(2L)).thenReturn(Optional.of(applicant));
        when(jobApplicationRepository.existsByJobIdAndApplicantId(1L, 2L)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jobApplicationService.applyForJob(1L, 2L, "resume.pdf", "cover letter"));
        assertEquals("You have already applied for this job", exception.getMessage());
    }

    @Test
    void getApplicationsByJob_Success() {
        User recruiter = new User();
        recruiter.setId(3L);
        job.setPostedBy(recruiter);

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(jobApplicationRepository.findByJobId(1L)).thenReturn(List.of(application));

        List<JobApplication> applications = jobApplicationService.getApplicationsByJob(1L, 3L);

        assertEquals(1, applications.size());
        verify(jobApplicationRepository, times(1)).findByJobId(1L);
    }

    @Test
    void testGetApplicationsByJob_Unauthorized() {
        User recruiter = new User();
        recruiter.setId(4L);
        job.setPostedBy(recruiter);

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jobApplicationService.getApplicationsByJob(1L, 3L)); // Wrong recruiter ID
        assertEquals("You are not authorized to view applications for this job", exception.getMessage());
    }

    @Test
    void testSearchApplicationsByApplicantName() {
        when(jobApplicationRepository.findByApplicantFullNameContainingIgnoreCase("Test"))
                .thenReturn(List.of(application));

        List<JobApplication> applications = jobApplicationService.searchApplicationsByApplicantName("Test");
        assertEquals(1, applications.size());
    }

    @Test
    void testGetApplicationsByApplicant() {
        when(jobApplicationRepository.findByApplicantEmail("test@applicant.com"))
                .thenReturn(List.of(application));

        List<JobApplication> applications = jobApplicationService.getApplicationsByApplicant("test@applicant.com");
        assertEquals(1, applications.size());
    }

    @Test
    void testWithdrawApplication_Success() {
        when(jobApplicationRepository.findById(10L)).thenReturn(Optional.of(application));
        jobApplicationService.withdrawApplication(10L, 2L);
        verify(jobApplicationRepository, times(1)).delete(application);
    }

    @Test
    void testWithdrawApplication_Unauthorized() {
        User otherUser = new User();
        otherUser.setId(99L);
        application.setApplicant(otherUser);

        when(jobApplicationRepository.findById(10L)).thenReturn(Optional.of(application));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jobApplicationService.withdrawApplication(10L, 2L));

        assertEquals("You are not authorized to withdraw this application", exception.getMessage());
    }
}

