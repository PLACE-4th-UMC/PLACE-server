package com.umc.place.story.controller;

import com.umc.place.comment.dto.CommentUploadReqDto;
import com.umc.place.comment.dto.CommentUploadResDto;
import com.umc.place.comment.service.CommentService;
import com.umc.place.common.BaseException;
import com.umc.place.common.BaseResponse;
import com.umc.place.story.dto.StoryDetailResponseDto;
import com.umc.place.story.dto.StoryUploadRequestDto;
import com.umc.place.story.dto.StoryUploadResponseDto;
import com.umc.place.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.umc.place.common.BaseResponseStatus.NULL_STORY;
import static com.umc.place.common.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
public class StoryController {

    private final StoryService storyService;
    private final CommentService commentService;

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

    @PostMapping("/{storyIdx}/comment")
    public BaseResponse<CommentUploadResDto> uploadStoryComment(@PathVariable Long storyIdx,
                                                                @RequestBody CommentUploadReqDto reqDto,
                                                                @RequestParam Long userId) {
        try {
            return new BaseResponse<>(commentService.uploadComment(storyIdx, reqDto, userId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/{storyIdx}/like")
    public BaseResponse<Void> likeStory(@PathVariable Long storyIdx,
                                        @RequestParam Long userIdx) {
        try {
            storyService.likeStory(storyIdx, userIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/uploadView")
    public BaseResponse<StoryUploadResponseDto> getStoryUploadView(@RequestParam Long userId) {
        try {
            StoryUploadResponseDto storyView = storyService.getStoryView(userId);
            if (storyView.getLatestStoryName().isBlank()) { // 유저가 스토리를 업로드하지 않았다면
                return new BaseResponse<>(storyView, NULL_STORY);
            }
            return new BaseResponse<>(storyView);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
