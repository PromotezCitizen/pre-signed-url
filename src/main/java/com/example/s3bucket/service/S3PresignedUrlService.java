package com.example.s3bucket.service;

import com.example.s3bucket.domain.Image;
import com.example.s3bucket.dto.S3Metadata;
import com.example.s3bucket.dto.S3UploadPassedDto;
import com.example.s3bucket.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3PresignedUrlService {
    private final S3Service s3Service;
    private final RedisService redisService;
    private final ImageRepository imageRepository;

    public String generatePresignedUrl(S3Metadata s3Metadata) {
        String objectKey = String.format(
                "%s/%s.%s",
                s3Metadata.getPath(),
                UUID.randomUUID(),
                s3Metadata.getType()
        );
        String presignedUrl = s3Service.generatePresignedUrl(objectKey, s3Metadata.getSize(), s3Metadata.getMime());
        redisService.save(presignedUrl, String.format("%s-%d", s3Metadata.getType(), s3Metadata.getSize()), 5, "MIN");
        return presignedUrl;
    }

    public boolean checkPresignedUrl(S3UploadPassedDto uploadPassedDto) throws URISyntaxException {
        if (redisService.get(uploadPassedDto.getUrl()) == null) return false;

        URI uri = new URI(uploadPassedDto.getUrl());

        redisService.remove(uploadPassedDto.getUrl());
        Image image = Image.builder()
                .url(uri.getPath())
                .build();
        imageRepository.save(image);
        return true;
    }
}
