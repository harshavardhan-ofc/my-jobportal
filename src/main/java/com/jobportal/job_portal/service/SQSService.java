package com.jobportal.job_portal.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SQSService {

    private final SqsClient sqsClient;
    private final String queueUrl = "https://sqs.us-east-1.amazonaws.com/093326771934/jobportal-queue";

    public SQSService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void sendMessageToQueue(String name, String email) {
        try {
            String messageBody = "{\"name\":\"" + name + "\", \"email\":\"" + email + "\"}";

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .build();

            sqsClient.sendMessage(request);
            System.out.println("Message sent to SQS: " + messageBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
