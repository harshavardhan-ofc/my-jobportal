package com.jobportal.job_portal.controller;

import com.jobportal.job_portal.entity.JobApplication;
import com.jobportal.job_portal.service.JobApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/jobs-searchByName")
@RequestMapping("/api/job-applications")
public class SearchApplicationController {

    private final JobApplicationService jobApplicationService;

    public SearchApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    // Search job applications by applicant name
    @GetMapping
    public List<JobApplication> searchApplications(@RequestParam String applicantName) {
        return jobApplicationService.searchApplicationsByApplicantName(applicantName);
    }
}
