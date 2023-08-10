package com.umc.place.common.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.umc.place.common.service.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@RestController
public class FileUploadController {

    private final S3Upload s3Upload;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(MultipartFile[] multipartFileLis) throws IOException {
        return ResponseEntity.ok(
                s3Upload.upload(multipartFileLis)
        );
    }
}