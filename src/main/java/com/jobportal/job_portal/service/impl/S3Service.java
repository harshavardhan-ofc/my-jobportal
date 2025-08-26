package com.jobportal.job_portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;

    public S3Service(
            @Value("${aws.accessKeyId}") String accessKey,
            @Value("${aws.secretAccessKey}") String secretKey,
            @Value("${aws.region}") String regionName) {

        Region region = Region.of(regionName);

        this.s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .httpClientBuilder(ApacheHttpClient.builder())
                .serviceConfiguration(
                        S3Configuration.builder()
                                .pathStyleAccessEnabled(false)  // Virtual-hosted style
                                .build()
                )
                .build();
    }

    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();;
//        file.getOriginalFilename();
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        return key;
    }
}

//package com.jobportal.job_portal.service.impl;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.http.apache.ApacheHttpClient;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.S3Configuration;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.presigner.S3Presigner;
//import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
//
//import java.io.IOException;
//import java.time.Duration;
//import java.util.UUID;
//
//@Service
//public class S3Service {
//
//    private final S3Client s3Client;
//    private final S3Presigner s3Presigner;
//    private final Region region;
//
//    public S3Service(
//            @Value("${aws.accessKeyId}") String accessKey,
//            @Value("${aws.secretAccessKey}") String secretKey,
//            @Value("${aws.region}") String regionName) {
//
//        this.region = Region.of(regionName);
//
//        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
//
//        this.s3Client = S3Client.builder()
//                .region(region)
//                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
//                .httpClientBuilder(ApacheHttpClient.builder())
//                .serviceConfiguration(
//                        S3Configuration.builder()
//                                .pathStyleAccessEnabled(false)  // Use virtual-hosted-style
//                                .build()
//                )
//                .build();
//
//        this.s3Presigner = S3Presigner.builder()
//                .region(region)
//                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
//                .build();
//    }
//
//    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
//        // Create a unique key for the uploaded file
//        String key = UUID.randomUUID() + "_" + "file uploaded";
//
//        // Upload file to S3
//        s3Client.putObject(
//                PutObjectRequest.builder()
//                        .bucket(bucketName)
//                        .key(key)
//                        .build(),
//                RequestBody.fromBytes(file.getBytes())
//        );
//
//        // Return a pre-signed URL for secure download
//        return generatePresignedUrl(bucketName, key);
//    }
//
//    private String generatePresignedUrl(String bucketName, String key) {
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(key)
//                .build();
//
//        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(60)) // Link valid for 60 minutes
//                .getObjectRequest(getObjectRequest)
//                .build();
//
//        return s3Presigner.presignGetObject(presignRequest)
//                .url()
//                .toString();
//    }
//}
