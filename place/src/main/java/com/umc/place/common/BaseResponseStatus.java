package com.umc.place.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    NULL_STORY(true, 1001, "해당 유저가 생성한 스토리가 아직 없습니다."),

    /**
     * 2000: Request 오류
     */
    INVALID_STORY_IDX(false, 2100, "잘못된 스토리 Idx 입니다"),

    // user(2000~2099)
    INVALID_USER_IDX(false, 2000, "잘못된 user Idx 입니다."),

    // story(2100~2199)

    // exhibition(2200~2299)
    INVALID_EXHIBITION_IDX(false, 2200, "잘못된 전시회 Idx 입니다."),
    INVALID_CATEGORY(false, 2201, "잘못된 카테고리입니다."),
    NULL_EXHIBITION_LIKE(false, 2202, "전시회 좋아요 수가 이미 0입니다."),

    // comment(2300~2399)

    /**
     * 3000: Response 오류
     */
    // user(3000~3099)

    // story(3100~3199)

    // exhibition(3200~3299)
    NULL_EXHIBITION(false, 3200, "전시회가 없습니다."),

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