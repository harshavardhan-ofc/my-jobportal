//package com.jobportal.job_portal.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.ses.SesClient;
//import software.amazon.awssdk.services.ses.model.*;
//
//@Service
//@RequiredArgsConstructor
//public class SesEmailService {
//
//    private final SesClient sesClient;
//
//    public void sendWelcomeEmail(String toEmail, String name) {
//        String body = "Hi " + name + ",\n\nWelcome to Job Portal! Your account has been created successfully.";
//
//        SendEmailRequest emailRequest = SendEmailRequest.builder()
//                .destination(Destination.builder().toAddresses(toEmail).build())
//                .message(Message.builder()
//                        .subject(Content.builder().data("Welcome to Job Portal").build())
//                        .body(Body.builder()
//                                .text(Content.builder().data(body).build())
//                                .build())
//                        .build())
//                .source("nchharsha789@gmail.com")
//                .build();
//
//        sesClient.sendEmail(emailRequest);
//        System.out.println("SES Welcome Email sent to: " + toEmail);
//    }
//}
