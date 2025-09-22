//package com.jobportal.job_portal.service.impl;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
//
//@Service
//public class SecretsManagerService {
//
//    private final SecretsManagerClient secretsManagerClient;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public SecretsManagerService(SecretsManagerClient secretsManagerClient){
//        this.secretsManagerClient = secretsManagerClient;
//    }
//
//    public DbCredentials getDbCredentials(String secretName) throws Exception {
//        GetSecretValueRequest request=GetSecretValueRequest.builder()
//                .secretId(secretName)
//                .build();
//
//
//        GetSecretValueResponse response = secretsManagerClient.getSecretValue(request);
//        String secretString = response.secretString();
//
//        JsonNode node = objectMapper.readTree(secretString);
//
//        return new DbCredentials(
//                node.get("username").asText(),
//                node.get("password").asText(),
//                node.get("url").asText()
//        );
//    }
//
//    public static class DbCredentials {
//        public String username;
//        public String password;
//        public String url;
//
//        public DbCredentials(String username, String password, String url) {
//            this.username = username;
//            this.password = password;
//            this.url = url;
//        }
//    }
//}
