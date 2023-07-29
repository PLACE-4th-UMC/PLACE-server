package com.umc.place.comment.dto;

import com.umc.place.comment.entity.Comment;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentResDto {

    // 이후 디자인에 따라 변동 가능성 있음.
    private String userImg;
    private String userNickName;
    private String content;
    private LocalDateTime createdDate;

    @Builder
    public CommentResDto(String userImg, String userName, String content, LocalDateTime createdDate) {
        this.userImg = userImg;
        this.userNickName = userName;
        this.content = content;
        this.createdDate = createdDate;
    }

    public CommentResDto(Comment comment) {
        this.userImg = comment.getUser().getUserImg();
        this.userNickName = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.createdDate = comment.getCreatedDate();
    }
}
