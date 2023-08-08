package com.umc.place.exhibition.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetExhibitionsRes {
    private Long exhibitionIdx;
    private String exhibitionName;
    private String exhibitionImg;
    private String location;
}
