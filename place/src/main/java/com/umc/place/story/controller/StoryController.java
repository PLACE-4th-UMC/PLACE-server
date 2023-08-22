package com.umc.place.story.controller;

import com.umc.place.comment.dto.CommentUploadReqDto;
import com.umc.place.comment.dto.CommentUploadResDto;
import com.umc.place.comment.service.CommentService;
import com.umc.place.common.BaseException;
import com.umc.place.common.BaseResponse;
import com.umc.place.exhibition.dto.SearchExhibitionsByNameResDto;
import com.umc.place.story.dto.StoryDetailResponseDto;
import com.umc.place.story.dto.StoryUploadRequestDto;
import com.umc.place.story.dto.StoryUploadResponseDto;
import com.umc.place.story.service.StoryService;
import com.umc.place.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.umc.place.common.BaseResponseStatus.NULL_STORY;
import static com.umc.place.common.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
public class StoryController {

    private final StoryService storyService;
    private final CommentService commentService;
    private final AuthService authService;

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

    @GetMapping("/search")
    public BaseResponse<SearchExhibitionsByNameResDto> getExhibitionWhenUploadStory(
            @PageableDefault(size = 5) Pageable pageable,
            @RequestParam(required = false) String searchWord) {
        try {
            return new BaseResponse<>(storyService.searchExhibitionByName(searchWord, pageable));
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

    @DeleteMapping("{storyIdx}/{commentIdx}")
    public BaseResponse<Void> deleteStoryComment(@PathVariable Long storyIdx,
                                                 @PathVariable Long commentIdx) {
        try {
            // TODO: 로그인 확인 로직 추가 - 비로그인 상태면 접근 막기
            return new BaseResponse<>(commentService.deleteComment(storyIdx, commentIdx, authService.getUserIdx()));
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
