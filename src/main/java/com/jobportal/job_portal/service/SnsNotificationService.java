//package com.jobportal.job_portal.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.services.sns.SnsClient;
//import software.amazon.awssdk.services.sns.model.SubscribeRequest;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class SnsNotificationService {
//
//    private final SnsClient snsClient;
//
//    @Value("${aws.sns.topic.arn}")
//    private String topicArn;
//
//    public void subscribeUser(String email) {
//        try {
//            SubscribeRequest request = SubscribeRequest.builder()
//                    .protocol("email")
//                    .endpoint(email)
//                    .topicArn(topicArn)
//                    .returnSubscriptionArn(true)
//                    .build();
//            snsClient.subscribe(request);
//            log.info("User subscribed to SNS: " + email);
//        } catch (Exception e) {
//            log.error("Error subscribing email: " + e.getMessage());
//        }
//    }
//}
