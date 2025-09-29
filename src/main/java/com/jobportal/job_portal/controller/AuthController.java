package com.jobportal.job_portal.controller;

import com.jobportal.job_portal.dto.*;
import com.jobportal.job_portal.entity.User;
import com.jobportal.job_portal.repository.UserRepository;
import com.jobportal.job_portal.security.JwtService;
import com.jobportal.job_portal.service.SQSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final SQSService sqsService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest req) {
        if (users.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        String role = (req.getRole() == null || req.getRole().isBlank()) ? "APPLICANT" : req.getRole().toUpperCase();

        User u = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .role(role)
                .password(encoder.encode(req.getPassword()))
                .username(req.getEmail())
                .build();
        users.save(u);

        // Send name + email to SQS after successful save
        sqsService.sendMessageToQueue(u.getFullName(), u.getEmail());

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        String token = jwt.generateToken(req.getEmail(),
                Map.of("role", users.findByEmail(req.getEmail()).map(User::getRole).orElse("APPLICANT")));
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
