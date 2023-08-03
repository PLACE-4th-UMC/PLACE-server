package com.umc.place.user.dto;

import lombok.Data;


@Data
public class PatchProfileReq {
    private String nickname;
    private String userImg;
    private String email;
    private String location;
}
