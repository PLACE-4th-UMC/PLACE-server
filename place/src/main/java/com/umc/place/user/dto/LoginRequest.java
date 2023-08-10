package com.umc.place.user.dto;

import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
public class LoginRequest {
    @NotNull
    private String code;
    private String provider;
}
