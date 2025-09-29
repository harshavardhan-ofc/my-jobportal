//package com.jobportal.job_portal.lambda;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import org.json.JSONObject;
//import software.amazon.awssdk.services.ses.SesClient;
//import software.amazon.awssdk.services.ses.model.Body;
//import software.amazon.awssdk.services.ses.model.Content;
//import software.amazon.awssdk.services.ses.model.Destination;
//import software.amazon.awssdk.services.ses.model.Message;
//import software.amazon.awssdk.services.ses.model.SendEmailRequest;
//import software.amazon.awssdk.services.ses.model.SendEmailResponse;
//
//import software.amazon.awssdk.services.sns.SnsClient;
//import software.amazon.awssdk.services.sns.model.PublishRequest;
//import software.amazon.awssdk.services.sns.model.PublishResponse;
//
//public class RegistrationLambdaHandler implements RequestHandler<Object, String> {
//
//    // AWS SDK v2 clients
//    private final SesClient sesClient = SesClient.builder().build();
//    private final SnsClient snsClient = SnsClient.builder().build();
//
//    private final String snsTopicArn = "arn:aws:sns:us-east-1:093326771934:jobportal-topic"; // change to your topic
//
//    @Override
//    public String handleRequest(Object event, Context context) {
//        try {
//            // Convert SQS message to JSON
//            JSONObject messageJson = new JSONObject(event.toString());
//            String body = messageJson.getJSONArray("Records").getJSONObject(0).getString("body");
//
//            JSONObject bodyJson = new JSONObject(body);
//            String name = bodyJson.getString("name");
//            String email = bodyJson.getString("email");
//
//            // 1️⃣ Send Welcome Email
//            sendEmail(email, name);
//
//            // 2️⃣ Publish notification to SNS
//            publishSns(email);
//
//            return "Processed successfully";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error: " + e.getMessage();
//        }
//    }
//
//    private void sendEmail(String email, String name) {
//        String subjectText = "Welcome to Job Portal!";
//        String bodyText = "Hi " + name + ",\n\nWelcome to our Job Portal. You will now receive job notifications.";
//
//        SendEmailRequest emailRequest = SendEmailRequest.builder()
//                .destination(Destination.builder()
//                        .toAddresses(email)
//                        .build())
//                .message(Message.builder()
//                        .subject(Content.builder()
//                                .data(subjectText)
//                                .build())
//                        .body(Body.builder()
//                                .text(Content.builder()
//                                        .data(bodyText)
//                                        .build())
//                                .build())
//                        .build())
//                .source("nchharsha789@gmail.com") // SES verified email
//                .build();
//
//        SendEmailResponse response = sesClient.sendEmail(emailRequest);
//        System.out.println("SES Email sent! MessageId: " + response.messageId());
//    }
//
//    private void publishSns(String email) {
//        PublishRequest request = PublishRequest.builder()
//                .topicArn(snsTopicArn)
//                .message("New user registered: " + email)
//                .build();
//
//        PublishResponse response = snsClient.publish(request);
//        System.out.println("SNS message published! MessageId: " + response.messageId());
//    }
//}
