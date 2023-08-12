package com.umc.place.home.dto;

import com.umc.place.magazine.dto.GetMagazineDetailRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetHomeUserRecentDetailRes {
//    private String exhibitionImg;
//    private String exhibitionName;
//    private String location;
//    private String artist;

    private List<GetHomeUserRecentDetailRes.UserRecentList> userRecentLists;


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

}
