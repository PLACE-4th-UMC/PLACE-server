package com.umc.place.exhibition.controller;

import com.umc.place.common.BaseException;
import com.umc.place.common.BaseResponse;
import com.umc.place.exhibition.dto.GetExhibitionDetailRes;
import com.umc.place.exhibition.dto.GetExhibitionsRes;
import com.umc.place.exhibition.service.ExhibitionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
     * [GET] 전시회 목록 조회
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<Page<GetExhibitionsRes>> getExhibitions(@RequestParam(required = false) String categoryName, @PageableDefault(size = 20) Pageable page) {
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
    public BaseResponse<GetExhibitionDetailRes> getExhibitionDetail(@PathVariable("exhibitionIdx") Long exhibitionIdx, @PathVariable("userIdx") Long userIdx,
                                                                    HttpServletRequest request, HttpServletResponse response) { // TODO: authService 생성 후 userIdx 삭제
        try {
            Cookie oldCookie = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("exhibitionView")) {
                        oldCookie = cookie;
                    }
                }
            }
            if (oldCookie != null) { // 쿠키가 존재하면
                if (!oldCookie.getValue().contains("[" + exhibitionIdx.toString() + "]")) { // 현재 조회한 전시회가 쿠키 목록에 없으면
                    exhibitionService.updateViewCount(exhibitionIdx); // 조회수+1
                    oldCookie.setValue(oldCookie.getValue() + "_[" + exhibitionIdx + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24); // 쿠키 생명주기: 24시간 설정
                    response.addCookie(oldCookie); // 쿠키 목록에 현재 조회한 전시회 추가
                }
            } else { // 아무 쿠키도 없으면
                this.exhibitionService.updateViewCount(exhibitionIdx); // 조회수+1
                Cookie newCookie = new Cookie("exhibitionView", "[" + exhibitionIdx + "]"); // 쿠키 생성
                newCookie.setPath("/");
                newCookie.setMaxAge(60 * 60 * 24); // 쿠키 생명주기: 24시간 설정
                response.addCookie(newCookie);
            }
            return new BaseResponse<>(exhibitionService.getExhibitionDetail(exhibitionIdx, userIdx)); // TODO: userIdx -> authService.getUserIdx() 수정
        } catch (BaseException e) {
            throw new RuntimeException(e);
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
