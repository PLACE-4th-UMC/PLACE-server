package com.umc.place.exhibition.entity;

import lombok.Getter;

@Getter
public enum Category {
    DESIGN(1, "디자인"),
    INTERACTIVE(2, "체험형"), 
    PHOTO(3, "사진"),
    INSTALLATION(4, "설치미술"),
    ETC(5, "기타");

    private final int num;
    private final String category;

    Category(int num, String category) {
        this.num = num;
        this.category = category;
    }
}
