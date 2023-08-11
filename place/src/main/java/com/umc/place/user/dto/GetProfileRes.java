package com.umc.place.user.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class GetProfileRes {
    private String userImg;
    private String nickname;
    private String comment;
    private int signupYear;

    public GetProfileRes(String userImg, String nickname, String comment, int signupYear) {
        this.userImg = userImg;
        this.nickname = nickname;
        this.comment = comment;
        this.signupYear = signupYear;
    }
}
