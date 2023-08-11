package com.umc.place.user.OAuth2.dto;

import lombok.Data;

@Data
public class GoogleTokenResponse {
    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
