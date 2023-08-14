package com.umc.place.user.dto;

import lombok.Data;


@Data
public class PatchProfileReq { //프로필 수정
    private String userImg;
    private String nickname;
    private String email;
    private String birthday;
    private String location;
}
