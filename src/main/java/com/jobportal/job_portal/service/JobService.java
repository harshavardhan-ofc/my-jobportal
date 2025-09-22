package com.jobportal.job_portal.service;

import com.jobportal.job_portal.dto.JobDto;
import java.util.List;

public interface JobService {
    JobDto createJob(JobDto jobDto, Long recruiterId);
    List<JobDto> getAllJobs();
    JobDto getJobById(Long jobId);
    void deleteJob(Long jobId, Long recruiterId);
    Long getRecruiterIdFromEmail(String email);

    // recruiter must own the job
}
