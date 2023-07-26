package com.umc.place.story.dto;

import com.umc.place.comment.dto.CommentResDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class StoryDetailResponseDto {
    private String exhibitionName;
    private String exhibitionAddress;
    private List<CommentResDto> comments = new ArrayList<>();

    // like 부분이 추가될 듯 함!!!

    @Builder
    public StoryDetailResponseDto(String exhibitionName, String exhibitionAddress, List<CommentResDto> comments) {
        this.exhibitionName = exhibitionName;
        this.exhibitionAddress = exhibitionAddress;
        this.comments = comments;
    }
}
