package com.jobportal.job_portal.repository;

import com.jobportal.job_portal.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByJobId(Long jobId);

    List<JobApplication> findByApplicantEmail(String applicantEmail);

    boolean existsByJobIdAndApplicantId(Long jobId, Long applicantId);

    List<JobApplication> findByApplicantFullNameContainingIgnoreCase(String name);
}
