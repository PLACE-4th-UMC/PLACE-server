package com.umc.place.user.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class PostNewUserReq {
    private String nickname;
    private String UserImg;
    private String email;
    private String location;
    private Date birthday;
}
