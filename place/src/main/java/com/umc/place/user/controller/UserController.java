package com.umc.place.user.controller;

import com.umc.place.common.BaseException;
import com.umc.place.common.BaseResponse;
import com.umc.place.user.OAuth2.dto.KakaoTokenResponse;
import com.umc.place.user.OAuth2.service.KakaoAuthService;
import com.umc.place.user.dto.PostUserRes;
import com.umc.place.user.dto.PostNewUserReq;
import com.umc.place.user.dto.PostNicknameReq;
import com.umc.place.user.dto.LoginRequest;
import com.umc.place.user.service.AuthService;
import com.umc.place.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.umc.place.common.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final KakaoAuthService kakaoAuthService;


    //카카오 소셜 로그인
    @ResponseBody
    @PostMapping("/login/kakao")
    public BaseResponse<?> login(@RequestBody LoginRequest loginRequest) throws IOException, BaseException {
        try{
            KakaoTokenResponse kakaoTokenResponse = kakaoAuthService.getKakaoToken(loginRequest.getCode());
            Long useridx = kakaoAuthService.getKakaoUserIdx(kakaoTokenResponse);
            PostUserRes postUserRes = userService.login(useridx, "카카오");
            return new BaseResponse<>(postUserRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //회원가입 후 정보 입력
    @ResponseBody
    @PostMapping("/signup")
    public BaseResponse<?> signup(@RequestBody PostNewUserReq postNewUserReq) {
        try{
            return new BaseResponse<>(userService.signup_UserInfo(authService.getUserIdx(), postNewUserReq));
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //닉네임 중복 확인
    @ResponseBody
    @PostMapping("/nickname")
    public BaseResponse<?> checkNickname(@RequestBody PostNicknameReq postNicknameReq) {
        try{
            userService.checkNickname(postNicknameReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //마이페이지 조회


    //회원 프로필 수정


    //회원 탈퇴


    //회원 로그아웃


    //AccessToken 재발급

}
