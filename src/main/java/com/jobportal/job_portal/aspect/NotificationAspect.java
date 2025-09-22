package com.jobportal.job_portal.aspect;

import com.jobportal.job_portal.dto.RegisterRequest;
import com.jobportal.job_portal.dto.JobDto;
import com.jobportal.job_portal.service.SesEmailService;
import com.jobportal.job_portal.service.SnsNotificationService;
import com.jobportal.job_portal.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class NotificationAspect {

    private final SesEmailService sesEmailService;
    private final SnsNotificationService snsService;
    private final NotificationService notificationService;


    @AfterReturning(pointcut = "execution(* com.jobportal.job_portal.controller.AuthController.register(..))",
            returning = "result")
    public void afterUserRegistration(JoinPoint joinPoint, Object result) {

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof RegisterRequest req) {
                String name = req.getFullName();
                String email = req.getEmail();




                log.info("Sending welcome email to: " + email);
                sesEmailService.sendWelcomeEmail(email, name);

                snsService.subscribeUser(email);
            }
        }
    }

    @AfterReturning(pointcut = "execution(* com.jobportal.job_portal.service.JobService.createJob(..))",
            returning = "jobDto")
    public void afterJobPosted(Object jobDto) {
        if (jobDto instanceof JobDto job) {
            notificationService.sendJobPostedNotification(job.getTitle());
        }
    }
}

