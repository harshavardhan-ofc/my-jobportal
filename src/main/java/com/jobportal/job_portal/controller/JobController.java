package com.jobportal.job_portal.controller;

import com.jobportal.job_portal.dto.JobDto;
import com.jobportal.job_portal.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

//    @PreAuthorize("hasRole('APPLICANT')")
    @GetMapping("/All")
    public ResponseEntity<List<JobDto>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobDto> getJobById(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobService.getJobById(jobId));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/create")
    public ResponseEntity<JobDto> createJob(@RequestBody JobDto jobDto, Principal principal) {
        // Recruiter ID is fetched from logged-in user
        Long recruiterId = jobService.getRecruiterIdFromEmail(principal.getName());
        return ResponseEntity.ok(jobService.createJob(jobDto, recruiterId));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @DeleteMapping("/delete/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable Long jobId, Principal principal) {
        Long recruiterId = jobService.getRecruiterIdFromEmail(principal.getName());
        jobService.deleteJob(jobId, recruiterId);
        return ResponseEntity.ok("Job deleted successfully");
    }
}
