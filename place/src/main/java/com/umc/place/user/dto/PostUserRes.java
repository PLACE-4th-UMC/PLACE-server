package com.umc.place.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PostUserRes {
    private final String accessToken;
    private final String refreshToken;

    @Builder
    public PostUserRes (String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

    }
}
