//package com.jobportal.job_portal.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import io.awspring.cloud.messaging.listener.annotation.SqsListener;
//
//@Service
//@RequiredArgsConstructor
//public class SqsListenerService {
//
//    private final SesEmailService sesEmailService;
//
//    @SqsListener("your-registration-queue-name") // replace with your queue name
//    public void processRegistrationQueue(String message) {
//        // Message format: email;fullName
//        String[] parts = message.split(";");
//        if (parts.length == 2) {
//            String email = parts[0];
//            String fullName = parts[1];
//
//            sesEmailService.sendWelcomeEmail(email, fullName);
//            System.out.println("Processed SQS message for user: " + email);
//        }
//    }
//}
