//package com.jobportal.job_portal.service;
//
//import com.jobportal.job_portal.service.impl.S3Service;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.mock.web.MockMultipartFile;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.core.sync.RequestBody;
//
//import java.io.IOException;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class S3ServiceTest {
//
//    @Mock
//    private S3Client s3Client;
//
//    @InjectMocks
//    private S3Service s3Service;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testUploadFile() throws IOException {
//
//        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello AWS".getBytes());
//
//
//        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
//        .thenReturn(null);
//
//        String key = s3Service.uploadFile(file, "test-bucket");
//
//
//        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
//        assert key.contains("test.txt");
//    }
//}

package com.jobportal.job_portal.service;

import com.jobportal.job_portal.service.impl.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadFile_Success() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello AWS".getBytes());
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(null);


        String key = s3Service.uploadFile(file, "test-bucket");


        assertNotNull(key);
        assertTrue(key.endsWith("test.txt") || key.contains("test.txt"));


        ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3Client, times(1)).putObject(requestCaptor.capture(), any(RequestBody.class));

        PutObjectRequest request = requestCaptor.getValue();
        assertEquals("test-bucket", request.bucket());
        assertTrue(request.key().contains("test.txt"));
    }

    @Test
    void testUploadFile_IOException() throws IOException {

        MockMultipartFile file = mock(MockMultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.txt");
        when(file.getBytes()).thenThrow(new IOException("Simulated IO error"));


        IOException exception = assertThrows(IOException.class, () -> s3Service.uploadFile(file, "test-bucket"));
        assertEquals("Simulated IO error", exception.getMessage());

        verify(s3Client, never()).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }
}
