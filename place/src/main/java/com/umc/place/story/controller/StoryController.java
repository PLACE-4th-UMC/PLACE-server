package com.umc.place.story.controller;

import com.umc.place.common.BaseException;
import com.umc.place.common.BaseResponse;
import com.umc.place.story.dto.StoryDetailResponseDto;
import com.umc.place.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
public class StoryController {

    private final StoryService storyService;

    @GetMapping("/{storyIdx}")
    public BaseResponse<StoryDetailResponseDto> getStoryDetail(@PathVariable Long storyIdx) {
        try {
            return new BaseResponse<>(storyService.getStoryDetail(storyIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
