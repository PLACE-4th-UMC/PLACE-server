package com.umc.place.story.controller;

import com.umc.place.comment.dto.CommentUploadReqDto;
import com.umc.place.comment.dto.CommentUploadResDto;
import com.umc.place.comment.service.CommentService;
import com.umc.place.common.BaseException;
import com.umc.place.common.BaseResponse;
import com.umc.place.common.service.S3Upload;
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

import java.io.IOException;

import static com.umc.place.common.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
public class StoryController {

    private final StoryService storyService;
    private final CommentService commentService;
    private final AuthService authService;

    private final S3Upload s3Upload;

    @PostMapping("")
    public BaseResponse<StoryUploadResponseDto> uploadStory(@RequestBody StoryUploadRequestDto storyUploadRequestDto) {
        try {
            // TODO: upload의 idx 부분, storyImgUrl 변경 필요
            s3Upload.upload(storyUploadRequestDto.getImgFile(), "story", 1L);
            return new BaseResponse<>(
                    storyService.uploadStory(storyUploadRequestDto, authService.getUserIdx(), ""));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        } catch (IOException e) {
            return new BaseResponse<>(S3_ERROR);
        }
    }

    @GetMapping("/{storyIdx}")
    public BaseResponse<StoryDetailResponseDto> getStoryDetail(@PathVariable Long storyIdx) {
        try {
            return new BaseResponse<>(storyService.getStoryDetail(storyIdx, authService.getUserIdx()));
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
                                                                @RequestBody CommentUploadReqDto reqDto) {
        try {
            return new BaseResponse<>(commentService.uploadComment(storyIdx, reqDto, authService.getUserIdx()));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/{storyIdx}/like")
    public BaseResponse<Void> likeStory(@PathVariable Long storyIdx) {
        try {
            storyService.likeStory(storyIdx, authService.getUserIdx());
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/uploadView")
    public BaseResponse<StoryUploadResponseDto> getStoryUploadView() {
        try {
            StoryUploadResponseDto storyView = storyService.getStoryView(authService.getUserIdx());
            if (storyView.getLatestStoryName().isBlank()) { // 유저가 스토리를 업로드하지 않았다면
                return new BaseResponse<>(storyView, NULL_STORY);
            }
            return new BaseResponse<>(storyView);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
