package com.umc.place.story.dto;

import com.umc.place.comment.dto.CommentResDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class StoryDetailResponseDto {
    private String storyImg;
    private String exhibitionName;
    private String exhibitionAddress;

    private String storyOwnerImg;
    private List<CommentResDto> comments = new ArrayList<>();

    private Boolean isLiked;

    @Builder
    public StoryDetailResponseDto(String storyImg, String exhibitionName, String exhibitionAddress,
                                  String storyOwnerImg, List<CommentResDto> comments, Boolean isLiked) {
        this.storyImg = storyImg;
        this.exhibitionName = exhibitionName;
        this.exhibitionAddress = exhibitionAddress;
        this.storyOwnerImg = storyOwnerImg;
        this.comments = comments;
        this.isLiked = isLiked;
    }
}
