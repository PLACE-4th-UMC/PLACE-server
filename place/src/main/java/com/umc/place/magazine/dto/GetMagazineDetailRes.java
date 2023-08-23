package com.umc.place.magazine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetMagazineDetailRes {
    private List<LikeList> likeLists;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class LikeList{
        private String img;
        private String location;
    }
}
