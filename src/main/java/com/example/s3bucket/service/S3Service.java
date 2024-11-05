package com.example.s3bucket.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final long SECOND = 1000;
    private final long MINUTE = 60 * SECOND;

    public String generatePresignedUrl(String objectKey, long contentLength, String mime) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(new Date(System.currentTimeMillis() + 5 * MINUTE));

        generatePresignedUrlRequest
                .putCustomRequestHeader("Content-Length", String.valueOf(contentLength));

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }
}
