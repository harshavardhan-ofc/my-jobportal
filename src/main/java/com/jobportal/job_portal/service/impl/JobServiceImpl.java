package com.jobportal.job_portal.service.impl;

import com.jobportal.job_portal.dto.JobDto;
import com.jobportal.job_portal.entity.Job;
import com.jobportal.job_portal.entity.User;
import com.jobportal.job_portal.repository.JobRepository;
import com.jobportal.job_portal.repository.UserRepository;
import com.jobportal.job_portal.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Override
    public JobDto createJob(JobDto jobDto, Long recruiterId) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        if (!"RECRUITER".equalsIgnoreCase(recruiter.getRole())) {
            throw new RuntimeException("Only recruiters can create jobs");
        }

        Job job = Job.builder()
                .title(jobDto.getTitle())
                .description(jobDto.getDescription())
                .companyName(jobDto.getCompanyName())
                .location(jobDto.getLocation())
                .employmentType(jobDto.getEmploymentType())
                .salary(jobDto.getSalary())
                .postedAt(LocalDateTime.now())
                .postedBy(recruiter)
                .build();

        Job savedJob = jobRepository.save(job);

        return mapToDto(savedJob);
    }
    @Override
    public Long getRecruiterIdFromEmail(String email) {
        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        if (!"RECRUITER".equalsIgnoreCase(recruiter.getRole())) {
            throw new RuntimeException("User is not a recruiter");
        }
        return recruiter.getId();
    }


    @Override
    public List<JobDto> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public JobDto getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return mapToDto(job);
    }

    @Override
    public void deleteJob(Long jobId, Long recruiterId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getPostedBy().getId().equals(recruiterId)) {
            throw new RuntimeException("You are not authorized to delete this job");
        }

        jobRepository.delete(job);
    }

    private JobDto mapToDto(Job job) {
        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .companyName(job.getCompanyName())
                .location(job.getLocation())
                .employmentType(job.getEmploymentType())
                .salary(job.getSalary())
                .postedAt(job.getPostedAt())
                .postedBy(job.getPostedBy().getEmail())
                .build();
    }
}
