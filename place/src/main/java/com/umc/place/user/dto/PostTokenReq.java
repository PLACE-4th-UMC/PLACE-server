package com.umc.place.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostTokenReq {
    @NotBlank
    private String identifier;
    @NotBlank
    private String refreshToken;
}
