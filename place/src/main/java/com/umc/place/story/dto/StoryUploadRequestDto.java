package com.umc.place.story.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryUploadRequestDto {
    private Long exhibitionIdx;
    private MultipartFile imgFile;
}
