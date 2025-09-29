//package com.jobportal.job_portal.controller;
//
//import com.jobportal.job_portal.service.impl.S3Service;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
////import org.springframework.boot.test.mock.mockito.MockitoBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class FileUploadControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private S3Service s3Service;
//
//    @Test
//    void testFileUpload() throws Exception {
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                "testfile.txt",
//                MediaType.TEXT_PLAIN_VALUE,
//                "Hello S3".getBytes()
//        );
//
//        given(s3Service.uploadFile(any(), eq("my-jobportal-12345"))).willReturn("testfile.txt");
//
//        mockMvc.perform(multipart("/api/files/upload")
//                        .file(file)
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk())
//                .andExpect(content().string("File uploaded to S3! S3 key: testfile.txt"));
//    }
//}
