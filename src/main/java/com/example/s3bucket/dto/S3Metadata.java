package com.example.s3bucket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class S3Metadata {
    private String path;
    Long size;
    String mime;
    String type;
}
