package com.umc.place.exhibition.controller;

import com.umc.place.common.BaseException;
import com.umc.place.common.BaseResponse;
import com.umc.place.exhibition.dto.GetExhibitionDetailRes;
import com.umc.place.exhibition.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exhibitions")
@RequiredArgsConstructor
public class ExhibitionController {
    private final ExhibitionService exhibitionService;

    /**
     * [GET] 전시회 상세 조회
     */
    @ResponseBody
    @GetMapping("/{exhibitionIdx}/{userIdx}")
    public BaseResponse<GetExhibitionDetailRes> getExhibitionDetail(@PathVariable("exhibitionIdx") Long exhibitionIdx, @PathVariable Long userIdx) { // TODO: authService 생성 후 userIdx 삭제
        try {
            return new BaseResponse<>(exhibitionService.getExhibitionDetail(exhibitionIdx, userIdx)); // TODO: userIdx -> authService.getUserIdx() 수정
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
