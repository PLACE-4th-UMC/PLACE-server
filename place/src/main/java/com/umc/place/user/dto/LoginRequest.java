package com.umc.place.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank
    private String code;

    //private String provider;

    private String state; //네이버 로그인 시 필요
}
