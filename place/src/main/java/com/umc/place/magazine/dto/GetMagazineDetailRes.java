package com.umc.place.magazine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetMagazineDetailRes {
    private List<LikeList> likeLists;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class LikeList{
        private String img;
        private String location;
    }
}
