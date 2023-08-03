package com.umc.place.exhibition.controller;

import com.umc.place.common.BaseException;
import com.umc.place.common.BaseResponse;
import com.umc.place.exhibition.dto.GetExhibitionDetailRes;
import com.umc.place.exhibition.dto.GetExhibitionsRes;
import com.umc.place.exhibition.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.umc.place.common.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/exhibitions")
@RequiredArgsConstructor
public class ExhibitionController {
    private final ExhibitionService exhibitionService;

    /**
     * [GET]전시회 목록 조회
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<Page<GetExhibitionsRes>> getExhibitions(@RequestParam(required = false) String categoryName,
                                                                @PageableDefault(size = 20) Pageable page) // request param "sort="
    {
        try {
            if (categoryName.isBlank()) {
                return new BaseResponse<>(exhibitionService.getExhibitions(page)); // 전체 목록 조회 .getContent() 추가 시 content만 가져오기 가능
            } else return new BaseResponse<>(exhibitionService.getExhibitionsByCategory(categoryName, page)); // 카테고리 기반 조회

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [GET] 전시회 상세 조회
     */
    @ResponseBody
    @GetMapping("/{exhibitionIdx}/{userIdx}")
    public BaseResponse<GetExhibitionDetailRes> getExhibitionDetail(@PathVariable("exhibitionIdx") Long exhibitionIdx, @PathVariable("userIdx") Long userIdx) { // TODO: authService 생성 후 userIdx 삭제
        try {
            return new BaseResponse<>(exhibitionService.getExhibitionDetail(exhibitionIdx, userIdx)); // TODO: userIdx -> authService.getUserIdx() 수정
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [POST] 전시회 좋아요 누르기
     */
    @ResponseBody
    @PostMapping("/{exhibitionIdx}/{userIdx}")
    public BaseResponse<String> likeExhibition(@PathVariable("exhibitionIdx") Long exhibitionIdx, @PathVariable("userIdx") Long userIdx) { // TODO: authService 생성 후 userIdx 삭제
        try {
            exhibitionService.likeExhibition(exhibitionIdx, userIdx); // TODO: userIdx -> authService.getUserIdx() 수정
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
