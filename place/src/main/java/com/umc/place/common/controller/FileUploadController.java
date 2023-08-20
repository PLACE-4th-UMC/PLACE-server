package com.umc.place.common.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.umc.place.common.BaseException;
import com.umc.place.common.BaseResponse;
import com.umc.place.common.service.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@RestController
public class FileUploadController {

    private final S3Upload s3Upload;

    @PostMapping("/upload/{location}/{idx}")
    public BaseResponse<Object> uploadFile(MultipartFile multipartFile, @PathVariable String location, @PathVariable Long idx) throws IOException, BaseException {
        s3Upload.upload(multipartFile,location,idx);
        return new BaseResponse<>(ResponseEntity.ok("업로드 성공"));
    }


}