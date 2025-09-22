package com.jobportal.job_portal.controller;

import com.jobportal.job_portal.entity.JobApplication;
import com.jobportal.job_portal.repository.UserRepository;
import com.jobportal.job_portal.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('APPLICANT')")
    @PostMapping("/apply")
    public JobApplication applyForJob(@RequestParam Long jobId,
                                      @RequestParam String resumeUrl,
                                      @RequestParam String coverLetter,
                                      Principal principal) {
        Long applicantId = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
        return jobApplicationService.applyForJob(jobId, applicantId, resumeUrl, coverLetter);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/by-job/{jobId}")
    public List<JobApplication> getApplicationsByJob(@PathVariable Long jobId, Principal principal) {
        Long recruiterId = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
        return jobApplicationService.getApplicationsByJob(jobId, recruiterId);
    }

    @PreAuthorize("hasRole('APPLICANT')")
    @GetMapping("/my-applications")
    public List<JobApplication> getMyApplications(Principal principal) {
        return jobApplicationService.getApplicationsByApplicant(principal.getName());
    }

    @PreAuthorize("hasRole('APPLICANT')")
    @DeleteMapping("/withdraw/{applicationId}")
    public String withdrawApplication(@PathVariable Long applicationId, Principal principal) {
        Long applicantId = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
        jobApplicationService.withdrawApplication(applicationId, applicantId);
        return "Application withdrawn successfully!";
    }
}
