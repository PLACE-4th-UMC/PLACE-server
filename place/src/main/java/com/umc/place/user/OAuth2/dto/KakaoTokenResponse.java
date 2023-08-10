package com.umc.place.user.OAuth2.dto;

import lombok.Data;

@Data
public class KakaoTokenResponse {
    private String token_type;
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private int refresh_token_expires_in;
    private String scope;
}
