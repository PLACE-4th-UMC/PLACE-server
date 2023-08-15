package com.umc.place.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetHomeUserLikeDetailRes {
//    private String exhibitionImg;
//    private String exhibitionName;
//    private String location;
//    private String artist;

    private List<GetHomeUserLikeDetailRes.UserLikeList> userLikeLists;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserLikeList{
        private String exhibitionImg;
        private String exhibitionName;
        private String location;
        private String artist;
    }

}
