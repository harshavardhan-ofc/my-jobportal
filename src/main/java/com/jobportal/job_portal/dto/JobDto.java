package com.jobportal.job_portal.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {
    private Long id;
    private String title;
    private String description;
    private String companyName;
    private String location;
    private String employmentType;
    private Double salary;
    private LocalDateTime postedAt;
    private String postedBy; // Email or name of recruiter
}
