package com.umc.place.user.dto;

import com.umc.place.story.entity.Story;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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

}
