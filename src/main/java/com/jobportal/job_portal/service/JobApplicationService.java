package com.jobportal.job_portal.service;

import com.jobportal.job_portal.entity.JobApplication;
import java.util.List;

public interface JobApplicationService {
    JobApplication applyForJob(Long jobId, Long applicantId, String resumeUrl, String coverLetter);
    List<JobApplication> getApplicationsByJob(Long jobId, Long recruiterId);
    List<JobApplication> getApplicationsByApplicant(String applicantEmail);
    void withdrawApplication(Long applicationId, Long applicantId);
    List<JobApplication> searchApplicationsByApplicantName(String applicantName);

}
