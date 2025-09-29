//package com.jobportal.job_portal.service;
//
//import com.jobportal.job_portal.service.impl.SecretsManagerService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class SecretsManagerServiceTest {
//
//    @Mock
//    private SecretsManagerClient secretsManagerClient;
//
//    @InjectMocks
//    private SecretsManagerService secretsManagerService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetDbCredentials_Success() throws Exception {
//        String secretJson = "{ \"username\": \"testUser\", \"password\": \"testPass\", \"url\": \"jdbc:testUrl\" }";
//        GetSecretValueResponse mockResponse = GetSecretValueResponse.builder()
//                .secretString(secretJson)
//                .build();
//
//        when(secretsManagerClient.getSecretValue(any(GetSecretValueRequest.class)))
//                .thenReturn(mockResponse);
//
//        SecretsManagerService.DbCredentials credentials = secretsManagerService.getDbCredentials("testSecret");
//
//        assertEquals("testUser", credentials.username);
//        assertEquals("testPass", credentials.password);
//        assertEquals("jdbc:testUrl", credentials.url);
//
//
//        verify(secretsManagerClient, times(1)).getSecretValue(any(GetSecretValueRequest.class));
//    }
//
//    @Test
//    void testGetDbCredentials_InvalidJson() {
//
//        String secretJson = "{ \"wrongKey\": \"value\" }";
//        GetSecretValueResponse mockResponse = GetSecretValueResponse.builder()
//                .secretString(secretJson)
//                .build();
//
//        when(secretsManagerClient.getSecretValue(any(GetSecretValueRequest.class)))
//                .thenReturn(mockResponse);
//
//        Exception ex = assertThrows(NullPointerException.class, () ->
//                secretsManagerService.getDbCredentials("testSecret")
//        );
//        assertTrue(ex.getMessage().contains("Cannot invoke"));
//    }
//
//    @Test
//    void testGetDbCredentials_AwsException() {
//
//        when(secretsManagerClient.getSecretValue(any(GetSecretValueRequest.class)))
//                .thenThrow(new RuntimeException("AWS Service Down"));
//
//        RuntimeException ex = assertThrows(RuntimeException.class, () ->
//                secretsManagerService.getDbCredentials("testSecret")
//        );
//        assertEquals("AWS Service Down", ex.getMessage());
//    }
//}
