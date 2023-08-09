package com.umc.place.comment.dto;

import com.umc.place.comment.entity.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class CommentUploadResDto {
    private List<CommentResDto> comments;

    public CommentUploadResDto(List<Comment> comments) {
        this.comments = comments.stream().map(
                comment -> new CommentResDto(comment)
        ).collect(Collectors.toList());
    }
}
