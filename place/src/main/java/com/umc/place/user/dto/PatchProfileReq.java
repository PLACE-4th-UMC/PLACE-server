package com.umc.place.user.dto;

import lombok.Data;

import java.util.Date;


@Data
public class PatchProfileReq { //프로필 수정
    private String userImg;
    private String nickname;
    private String email;
    private Date birthday;
    private String location;
}
