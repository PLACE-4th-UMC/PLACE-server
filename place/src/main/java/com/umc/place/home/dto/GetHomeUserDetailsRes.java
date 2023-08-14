package com.umc.place.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetHomeUserDetailsRes {
    private List<GetHomeUserRecentDetailRes.UserRecentList> userRecentLists;
    private List<GetHomeUserLikeDetailRes.UserLikeList> userLikeLists;
    private List<GetHomeUserViewDetailRes.UserViewList> userViewLists;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserRecentList{
        private String exhibitionImg;
        private String exhibitionName;
        private String location;
        private String artist;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserViewList{
        private String exhibitionImg;
        private String exhibitionName;
        private String location;
        private String artist;
    }
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
