package com.example.s3bucket.repository;

import com.example.s3bucket.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
