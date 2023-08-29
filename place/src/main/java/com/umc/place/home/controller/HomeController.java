package com.umc.place.home.controller;

import com.umc.place.common.BaseException;
import com.umc.place.common.BaseResponse;
import com.umc.place.home.dto.GetHomeUserLikeDetailRes;
import com.umc.place.home.dto.GetHomeUserRecentDetailRes;
import com.umc.place.home.dto.GetHomeUserViewDetailRes;
import com.umc.place.home.service.HomeService;
import com.umc.place.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {
    private final HomeService homeService;
    private final AuthService authService;

    @ResponseBody
    @GetMapping("userRecent")
    public BaseResponse<GetHomeUserRecentDetailRes> getHomeUserRecentDetail() throws BaseException {

        if (authService.isMember()){    //로그인이 된 경우
            Long userIdx = authService.getUserIdx(); // 로그인을 한 유저 id 찾기
            return new BaseResponse<>(homeService.getHomeUserRecentDetail(userIdx));
        }else{
            return new BaseResponse<>(homeService.getHomeUserRecentDetail());
        }
    }
    @ResponseBody
    @GetMapping("userView")
    public BaseResponse<GetHomeUserViewDetailRes> getHomeUserViewDetailRes() throws BaseException {
        if (authService.isMember()) {    //로그인이 된 경우
            Long userIdx = authService.getUserIdx(); // 로그인을 한 유저 id 찾기
            return new BaseResponse<>(homeService.getHomeUserViewDetailRes(userIdx));
        }else{
            return new BaseResponse<>(homeService.getHomeUserViewDetailRes());
        }
    }
    @ResponseBody
    @GetMapping("userLike")
    public BaseResponse<GetHomeUserLikeDetailRes> getHomeUserLikeDetailRes() throws BaseException {
        if (authService.isMember()) {    //로그인이 된 경우
            Long userIdx = authService.getUserIdx(); // 로그인을 한 유저 id 찾기
            return new BaseResponse<>(homeService.getHomeUserLikeDetailRes(userIdx));
        }else{
            return new BaseResponse<>(homeService.getHomeUserLikeDetailRes());
        }
    }
}
