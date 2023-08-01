package com.umc.place.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000: Request 오류
     */
    // user(2000~2099)
    INVALID_USER_IDX(false, 2000, "잘못된 user Idx 입니다."),
    NULL_TOKEN(false, 2001, "토큰 값을 입력해주세요."),
    NULL_USER_IDX(false, 2002, "user Idx를 입력해주세요."),
    NULL_PROVIDER(false, 2003, "소셜 이름을 입력해주세요."),
    INVALID_PROVIDER(false, 2004, "잘못된 소셜 이름입니다."),
    ALREADY_WITHDRAW_USER(false, 2005, "이미 탈퇴한 회원입니다."),
    INVALID_TOKEN(false, 2006, "유효하지 않은 토큰 값입니다."),
    UNSUPPORTED_TOKEN(false, 2007, "잘못된 형식의 토큰 값입니다."),
    MALFORMED_TOKEN(false, 2008, "잘못된 구조의 토큰 값입니다."),

    // story(2100~2199)
    INVALID_STORY_IDX(false, 2100, "잘못된 스토리 Idx 입니다"),

    // exhibition(2200~2299)
    INVALID_EXHIBITION_IDX(false, 2200, "잘못된 전시회 Idx 입니다."),

    // comment(2300~2399)

    /**
     * 3000: Response 오류
     */
    // user(3000~3099)
    EXPIRED_TOKEN(false, 3000, "만료된 토큰 값입니다."),
    EXIST_NICKNAME(false, 3001, "이미 사용 중인 닉네임입니다."),

    // story(3100~3199)

    // exhibition(3200~3299)

    // comment(3300~3399)

  
    /**
     * 4000: DB, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패했습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}