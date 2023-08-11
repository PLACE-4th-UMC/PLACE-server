package com.umc.place.user.dto;

import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GetProfileRes {
    private String userImg;
    private String nickname;
    private String comment;
    private int signupYear;
    private List<Story> storyList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Story {
        private String storyImg;
        private String location;
        private String exhibition;
    }

}
