package com.umc.place.exhibition.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetExhibitionDetailRes {
    private String exhibitionName;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime startDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime endDate;
    private String exhibitionImg;
    private String operatingTime;
    private String location;
    private Integer fee;
    private String artist;
    private List<Story> stories;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Story {
        private Long storyIdx;
        private String storyImg;
        private String nickname;
        private String userImg;
        private Boolean heart;
    }
}
