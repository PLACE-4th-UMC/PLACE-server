package com.umc.place.user.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class PostNewUserReq {
    private String userImg;
    private String nickname;
    private String email;
    private Date birthday;
    private String location;
}
