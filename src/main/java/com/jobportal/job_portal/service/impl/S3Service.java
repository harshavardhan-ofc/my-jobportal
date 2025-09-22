//package com.jobportal.job_portal.service.impl;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.http.apache.ApacheHttpClient;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.S3Configuration;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Service
//public class S3Service {
//
//    private final S3Client s3Client;
//
//
//    public S3Service(
//            @Value("${aws.accessKeyId}") String accessKey,
//            @Value("${aws.secretAccessKey}") String secretKey,
//            @Value("${aws.region}") String regionName) {
//
//        Region region = Region.of(regionName);
//
//        this.s3Client = S3Client.builder()
//                .region(region)
//                .credentialsProvider(
//                        StaticCredentialsProvider.create(
//                                AwsBasicCredentials.create(accessKey, secretKey)
//                        )
//                )
//                .httpClientBuilder(ApacheHttpClient.builder())
//                .serviceConfiguration(
//                        S3Configuration.builder()
//                                .pathStyleAccessEnabled(false)  // Virtual-hosted style
//                                .build()
//                )
//                .build();
//    }
//
//    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
//        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();;
////        file.getOriginalFilename();
//        s3Client.putObject(
//                PutObjectRequest.builder()
//                        .bucket(bucketName)
//                        .key(key)
//                        .build(),
//                RequestBody.fromBytes(file.getBytes())
//        );
//
//        return key;
//    }
//}
package com.jobportal.job_portal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

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
