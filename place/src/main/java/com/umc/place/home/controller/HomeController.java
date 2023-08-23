package com.umc.place.home.controller;

import com.umc.place.common.BaseResponse;
import com.umc.place.home.dto.GetHomeUserLikeDetailRes;
import com.umc.place.home.dto.GetHomeUserRecentDetailRes;
import com.umc.place.home.dto.GetHomeUserViewDetailRes;
import com.umc.place.home.service.HomeService;
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

    @ResponseBody
    @GetMapping("userRecent")
    public BaseResponse<GetHomeUserRecentDetailRes> getHomeUserRecentDetail(){
        return new BaseResponse<>(homeService.getHomeUserRecentDetail());
    }
    @ResponseBody
    @GetMapping("userView")
    public BaseResponse<GetHomeUserViewDetailRes> getHomeUserViewDetailRes(){
        return new BaseResponse<>(homeService.getHomeUserViewDetailRes());
    }
    @ResponseBody
    @GetMapping("userLike")
    public BaseResponse<GetHomeUserLikeDetailRes> getHomeUserLikeDetailRes(){
        return new BaseResponse<>(homeService.getHomeUserLikeDetailRes());
    }
}
