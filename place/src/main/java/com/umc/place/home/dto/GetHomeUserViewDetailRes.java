package com.umc.place.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetHomeUserViewDetailRes {
//    private String exhibitionImg;
//    private String exhibitionName;
//    private String location;
//    private String artist;

    private List<GetHomeUserViewDetailRes.UserViewList> userViewLists;


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

}
