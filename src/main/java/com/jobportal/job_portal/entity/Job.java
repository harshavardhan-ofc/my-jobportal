package com.jobportal.job_portal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  title;
    private String description;
    private String companyName;
    private String location;
    private String employmentType;
    private Double salary;

    @Column(nullable = false, updatable = false)
    private LocalDateTime postedAt;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User postedBy;
}
