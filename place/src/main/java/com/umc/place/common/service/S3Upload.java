package com.umc.place.common.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.place.common.BaseException;
import com.umc.place.story.entity.Story;
import com.umc.place.story.repository.StoryRepository;
import com.umc.place.user.entity.User;
import com.umc.place.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.umc.place.common.BaseResponseStatus.INVALID_USER_IDX;
import static com.umc.place.common.Constant.ACTIVE;

@RequiredArgsConstructor
@Service
public class S3Upload {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;
    private final StoryRepository storyRepository;

    //https://jforj.tistory.com/261
    public String upload(MultipartFile multipartFile, String location) throws IOException, BaseException {

        String originalName = location + "/" + UUID.randomUUID() + multipartFile.getOriginalFilename(); // 파일 이름
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
        System.out.println("imagePath = " + imagePath);
        return imagePath;
    }
}

//        if (location.equals("profile")) { // 프로필일 경우
//            User user = userRepository.findByUserIdxAndStatusEquals(idx,ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER_IDX)); // 유저에 저장
//            user.setUserImg(imagePath);
//        }
//        if (location.equals("story")) { // 스토리일 경우
//            Story story = storyRepository.findById(idx).orElseThrow(() -> new BaseException(INVALID_USER_IDX)); // 스토리에 저장
//            story.setStoryImg(imagePath);
//        }


