//package com.jobportal.job_portal.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.services.sqs.SqsClient;
//import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
//
//@Service
//@RequiredArgsConstructor
//public class SqsSenderService {
//
//    private final SqsClient sqsClient;
//
//    @Value("${aws.sqs.registration-queue-url}")
//    private String registrationQueueUrl;
//
//    public void sendRegistrationToQueue(String email, String fullName) {
//        String messageBody = email + ";" + fullName;
//        SendMessageRequest request = SendMessageRequest.builder()
//                .queueUrl(registrationQueueUrl)
//                .messageBody(messageBody)
//                .build();
//        sqsClient.sendMessage(request);
//        System.out.println("Registration message sent to SQS for: " + email);
//    }
//}
