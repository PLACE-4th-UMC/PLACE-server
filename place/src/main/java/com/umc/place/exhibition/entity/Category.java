package com.umc.place.exhibition.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {
    DESIGN("design", "디자인 전시"),
    INTERACTIVE("interactive", "체험형 전시"),
    PHOTO("photo", "사진 전시"),
    INSTALLATION("installation", "설치형 전시"),
    ETC("etc", "기타");

    private final String categoryName;
    private final String description;
    Category(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }

    public static Category getCategoryByCategoryName(String categoryInput){
        return Arrays.stream(Category.values())
                .filter(category -> category.categoryName.equals(categoryInput))
                .findAny()
                .orElse(null);
    }
}
