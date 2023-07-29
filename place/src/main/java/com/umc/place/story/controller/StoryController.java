package com.umc.place.story.controller;

import com.umc.place.common.BaseException;
import com.umc.place.common.BaseResponse;
import com.umc.place.story.dto.StoryDetailResponseDto;
import com.umc.place.story.dto.StoryUploadRequestDto;
import com.umc.place.story.dto.StoryUploadResponseDto;
import com.umc.place.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
public class StoryController {

    private final StoryService storyService;

    @PostMapping("")
    public BaseResponse<StoryUploadResponseDto> uploadStory(@RequestBody StoryUploadRequestDto storyUploadRequestDto,
                                                            @RequestParam Long userId) {
        try {
            return new BaseResponse<>(storyService.uploadStory(storyUploadRequestDto, userId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{storyIdx}")
    public BaseResponse<StoryDetailResponseDto> getStoryDetail(@PathVariable Long storyIdx, @RequestParam Long userId) {
        try {
            return new BaseResponse<>(storyService.getStoryDetail(storyIdx, userId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
