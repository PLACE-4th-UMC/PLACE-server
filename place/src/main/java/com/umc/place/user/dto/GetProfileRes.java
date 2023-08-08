package com.umc.place.user.dto;

import com.umc.place.story.entity.Story;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@RequiredArgsConstructor
public class GetProfileRes {
    private String userImg;
    private String nickname;
    private String comment;
    private int signupYear;
    private Optional<Story> storyList;


    public GetProfileRes(String userImg, String nickname, String comment, int signupYear, Optional<Story> storyList) {
        this.userImg = userImg;
        this.nickname = nickname;
        this.comment = comment;
        this.signupYear = signupYear;
        this.storyList = storyList;
    }
}
