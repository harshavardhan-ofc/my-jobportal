//package com.jobportal.job_portal.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
//
//@Configuration
//public class AwsConfig {
//
//    @Bean
//    public SecretsManagerClient secretsManagerClient(){
//        return SecretsManagerClient.builder()
//                .region(Region.US_EAST_1)
//                .build();
//    }
//}
