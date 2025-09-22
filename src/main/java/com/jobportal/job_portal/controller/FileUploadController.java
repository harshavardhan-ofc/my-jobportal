package com.jobportal.job_portal.controller;

import com.jobportal.job_portal.service.impl.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String bucketName = "my-jb-bucket";
        String key = s3Service.uploadFile(file, bucketName);
        return "File uploaded to S3! S3 key: " + key;
    }
}
