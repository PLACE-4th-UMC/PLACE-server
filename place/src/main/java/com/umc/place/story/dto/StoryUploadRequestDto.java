package com.umc.place.story.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryUploadRequestDto {
    private Long exhibitionIdx;
    private String storyImg;
}
