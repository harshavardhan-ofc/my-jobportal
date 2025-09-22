//package com.jobportal.job_portal.config;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Value("${aws.secret.name}")
//    private String secretName;
//
//    @Bean
//    @Primary
//    public DataSource dataSource() throws Exception {
//
//        SecretsManagerClient client = SecretsManagerClient.create();
//        GetSecretValueResponse secretValue = client.getSecretValue(
//                GetSecretValueRequest.builder().secretId(secretName).build()
//        );
//
//        String secretJson = secretValue.secretString();
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode jsonNode = mapper.readTree(secretJson);
//
//        String url = jsonNode.get("url").asText();
//        String username = jsonNode.get("username").asText();
//        String password = jsonNode.get("password").asText();
//
//
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        dataSource.setDriverClassName("org.postgresql.Driver");
//
//        return dataSource;
//    }
//}
