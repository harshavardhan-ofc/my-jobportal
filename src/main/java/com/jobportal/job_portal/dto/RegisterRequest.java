package com.jobportal.job_portal.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String role; // Optional, defaults to APPLICANT if not given
}
