package com.umc.place.user.controller;

import com.umc.place.common.BaseException;
import com.umc.place.common.BaseResponse;
import com.umc.place.user.OAuth2.dto.GoogleTokenResponse;
import com.umc.place.user.OAuth2.dto.KakaoTokenResponse;
import com.umc.place.user.OAuth2.dto.NaverTokenResponse;
import com.umc.place.user.OAuth2.service.GoogleAuthService;
import com.umc.place.user.OAuth2.service.KakaoAuthService;
import com.umc.place.user.OAuth2.service.NaverAuthService;
import com.umc.place.user.dto.*;
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
    private final GoogleAuthService googleAuthService;
    private final NaverAuthService naverAuthService;

    //카카오 소셜 로그인
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<?> login(@RequestBody LoginRequest loginRequest) throws IOException {
        try{
            //카카오 소셜로그인
            if(loginRequest.getProvider().equals("카카오")){
                //인가코드 가져와서 카카오에서 access 토큰 받아오기
                KakaoTokenResponse kakaoTokenResponse = kakaoAuthService.getKakaoToken(loginRequest.getCode());
                //토큰으로 사용자 정보(식별자) 가져오기
                String identifier = kakaoAuthService.getKakaoIdentifier(kakaoTokenResponse);
                PostUserRes postUserRes = userService.login(identifier, "카카오");
                return new BaseResponse<>(postUserRes);
            }
            //구글 소셜로그인
            else if (loginRequest.getProvider().equals("구글")){
                GoogleTokenResponse googleTokenResponse = googleAuthService.getGoogleToken(loginRequest.getCode());
                String identifier = googleAuthService.getGoogleIdentifier(googleTokenResponse);
                PostUserRes postUserRes = userService.login(identifier, "구글");
                return new BaseResponse<>(postUserRes);
            }
            //네이버 소셜로그인
            else {
                //인가 코드, state로 access 토큰 가져오기
                NaverTokenResponse naverTokenResponse = naverAuthService.getNaverToken(loginRequest.getCode(), loginRequest.getState());
                String identifier = naverAuthService.getNaverIdentifier(naverTokenResponse);
                PostUserRes postUserRes = userService.login(identifier, "네이버");
                return new BaseResponse<>(postUserRes);
            }
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //회원가입 후 정보 입력
    @ResponseBody
    @PostMapping("/signup")
    public BaseResponse<?> signup(@RequestBody PostNewUserReq postNewUserReq) {
        try{
            return new BaseResponse<>(userService.signup_UserInfo(authService.getIdentifier(), postNewUserReq));
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

    //프로필 조회
    @ResponseBody
    @GetMapping("/profile")
    public BaseResponse<?> getProfile(){
        try{
            return new BaseResponse<>(userService.getProfile());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //회원 프로필 수정
    @ResponseBody
    @PatchMapping("/modify")
    public BaseResponse<?> patchProfile(@RequestBody PatchProfileReq patchProfileReq) {
        try{
            String identifier = authService.getIdentifier();
            userService.modifyProfile(identifier, patchProfileReq);
            return new BaseResponse<>(SUCCESS);
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //회원 탈퇴
    @DeleteMapping("/signout")
    public BaseResponse<?> signout() {
        try{
            String identifier = authService.getIdentifier();
            userService.signout(identifier);
            return new BaseResponse<>(SUCCESS);
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //회원 로그아웃
    @PatchMapping("/logout")
    public BaseResponse<?> logout() {
        try{
            String identifier = authService.getIdentifier();
            userService.logout(identifier);
            return new BaseResponse<>(SUCCESS);
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //AccessToken 재발급
    @ResponseBody
    @PostMapping("/reissue")
    public BaseResponse<?> reissueToken(@RequestBody PostTokenReq postTokenReq) {
        try {
            return new BaseResponse<>(userService.reissueToken(postTokenReq));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


}
