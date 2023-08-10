package com.umc.place.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Upload {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;
    ;

    //https://jforj.tistory.com/261
    public List<String> upload(MultipartFile[] multipartFileList) throws IOException {
        List<String> imagePathList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFileList) {
            String originalName = multipartFile.getOriginalFilename(); // 파일 이름
            long size = multipartFile.getSize(); // 파일 크기

            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(size);

            // S3에 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, originalName, multipartFile.getInputStream(), objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            String imagePath = amazonS3Client.getUrl(bucket, originalName).toString(); // 접근가능한 URL 가져오기
            imagePathList.add(imagePath);
        }
        return imagePathList;
    }
}

//        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
//
//        ObjectMetadata objMeta = new ObjectMetadata();
//        objMeta.setContentLength(multipartFile.getInputStream().available());
//
//        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);
//
//        return amazonS3.getUrl(bucket, s3FileName).toString();
//    for(MultipartFile multipartFile: multipartFileList) {
//        String originalName = multipartFile.getOriginalFilename(); // 파일 이름
//        long size = multipartFile.getSize(); // 파일 크기
//
//        ObjectMetadata objectMetaData = new ObjectMetadata();
//        objectMetaData.setContentType(multipartFile.getContentType());
//        objectMetaData.setContentLength(size);
//
//        // S3에 업로드
//        amazonS3Client.putObject(
//                new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData)
//                        .withCannedAcl(CannedAccessControlList.PublicRead)
//        );
//
//        String imagePath = amazonS3Client.getUrl(S3Bucket, originalName).toString(); // 접근가능한 URL 가져오기
//        imagePathList.add(imagePath);
//    }
//
//		return new ResponseEntity<Object>(imagePathList, HttpStatus.OK);
