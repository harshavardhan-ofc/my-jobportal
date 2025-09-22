package com.jobportal.job_portal.service.impl;

import com.jobportal.job_portal.entity.Job;
import com.jobportal.job_portal.entity.JobApplication;
import com.jobportal.job_portal.entity.User;
import com.jobportal.job_portal.repository.JobApplicationRepository;
import com.jobportal.job_portal.repository.JobRepository;
import com.jobportal.job_portal.repository.UserRepository;
import com.jobportal.job_portal.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Override
    public JobApplication applyForJob(Long jobId, Long applicantId, String resumeUrl, String coverLetter) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        User applicant = userRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        if (!"APPLICANT".equalsIgnoreCase(applicant.getRole())) {
            throw new RuntimeException("Only applicants can apply for jobs");
        }

        if (jobApplicationRepository.existsByJobIdAndApplicantId(jobId, applicantId)) {
            throw new RuntimeException("You have already applied for this job");
        }

        JobApplication application = JobApplication.builder()
                .job(job)
                .applicant(applicant)
                .resumeUrl(resumeUrl)
                .coverLetter(coverLetter)
                .appliedAt(LocalDateTime.now())
                .build();

        return jobApplicationRepository.save(application);
    }

    @Override
    public List<JobApplication> getApplicationsByJob(Long jobId, Long recruiterId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getPostedBy().getId().equals(recruiterId)) {
            throw new RuntimeException("You are not authorized to view applications for this job");
        }

        return jobApplicationRepository.findByJobId(jobId);
    }
    @Override
    public List<JobApplication> searchApplicationsByApplicantName(String applicantName) {
        return jobApplicationRepository.findByApplicantFullNameContainingIgnoreCase(applicantName);
    }


    @Override
    public List<JobApplication> getApplicationsByApplicant(String applicantEmail) {
        return jobApplicationRepository.findByApplicantEmail(applicantEmail);
    }

    @Override
    public void withdrawApplication(Long applicationId, Long applicantId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getApplicant().getId().equals(applicantId)) {
            throw new RuntimeException("You are not authorized to withdraw this application");
        }

        jobApplicationRepository.delete(application);
    }
}
