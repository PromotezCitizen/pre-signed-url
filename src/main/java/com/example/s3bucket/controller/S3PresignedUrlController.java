package com.example.s3bucket.controller;

import com.example.s3bucket.dto.S3Metadata;
import com.example.s3bucket.service.S3PresignedUrlService;
import com.example.s3bucket.dto.S3UploadPassedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
@CrossOrigin
public class S3PresignedUrlController {
    private final S3PresignedUrlService s3PresignedUrlService;
    // 총 2가지의 과정이 필요
    // presigned url을 만드는 endpoint -> 반환 -> 프론트에서 업로드
    // s3에서 프론트로 경로 전달 -> db에 저장하는 endpoint

    @GetMapping("/presigned-url")
    public String generatePresignedUrlV2(@ModelAttribute S3Metadata s3Metadata) {
        return s3PresignedUrlService.generatePresignedUrl(s3Metadata);
    }

    // presigned url로 저장한 파일의 경로
    @PostMapping("/presigned-url")
    public String uploadObjectUrl(@RequestBody() S3UploadPassedDto uploadPassedDto) {
        try {
            if (!s3PresignedUrlService.checkPresignedUrl(uploadPassedDto)) return "false";
            return "true";
        } catch (URISyntaxException e) {
            return e.getReason();
        }
    }
}
