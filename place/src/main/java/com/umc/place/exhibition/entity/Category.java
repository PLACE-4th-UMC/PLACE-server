package com.umc.place.exhibition.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {
    DESIGN("디자인"),
    INTERACTIVE("체험형"),
    PHOTO("사진"),
    INSTALLATION("설치미술"),
    ETC("기타");

    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }

//    public static Category getCategoryByCategoryName(String categoryInput){
//        return Arrays.stream(Category.values())
//                .filter(category -> category.getCategoryName().equals(categoryInput))
//                .findAny()
//                .orElse(null);
//    }
}
