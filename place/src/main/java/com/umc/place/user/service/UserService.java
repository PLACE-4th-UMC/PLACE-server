package com.umc.place.user.service;

import com.umc.place.common.BaseException;
import com.umc.place.user.dto.*;
import com.umc.place.user.entity.Provider;
import com.umc.place.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.umc.place.common.BaseResponseStatus.*;


@Service
@RequiredArgsConstructor //생성자 자동 생성
public class UserService {

    private final AuthService authService;
    private final JwtTokenService jwtTokenService;

    private PostUserRes signUpOrLogin(Long useridx, Provider provider) throws BaseException {
        //Provider provider = Provider.KAKAO;
        User user = authService.findUserByIdAndProvider(useridx, provider);

        //기존 회원이 아닐 경우 회원가입
        if (user == null)
            user = signup(useridx,provider); //provider 카카오로 설정
        //탈퇴한 회원일 경우
        if (user.getStatus().equals("inactive"))
            throw new BaseException(ALREADY_WITHDRAW_USER);
        //기존 회원이면 로그인 처리
        user.login(); //user status active로 바꾸기
        authService.saveUser(user); //userRepository에 user 저장
        return jwtTokenService.createToken(user); //user의 access token, refresh token 반환
    }

    //로그인
    public PostUserRes login(Long useridx, String provider) throws BaseException{
        try{
            if(Provider.getProviderByName(provider) == null)
                throw new BaseException(INVALID_PROVIDER);
            return signUpOrLogin(useridx, Provider.getProviderByName(provider));
        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //첫 로그인 시 user 생성 및 저장 (가입 연도 추가하기 createdDate)
    public User signup(Long userIdx, Provider provider) {
        User newuser = User.builder()
                .userIdx(userIdx)
                .provider(provider)
                .build();
        return authService.saveUser(newuser);
    }

    //회원 가입 후 사용자 정보 입력
    @Transactional(rollbackFor = Exception.class)
    public PostUserRes signup_UserInfo(Long userIdx, PostNewUserReq postNewUserReq) throws BaseException {
        try{
            User user = authService.findUserByIdAndStatus(userIdx, "active").orElseThrow(()->new BaseException(INVALID_USER_IDX));
            String accessToken = jwtTokenService.createAccessToken(userIdx);
            String refreshToken = jwtTokenService.createRefreshToken(userIdx);

            user.signup(postNewUserReq.getNickname(), postNewUserReq.getUserImg(), postNewUserReq.getBirthday(), postNewUserReq.getLocation());
            authService.saveUser(user); //repository에 저장

            return new PostUserRes(accessToken, refreshToken);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //닉네임 중복 확인하기
    public void checkNickname(PostNicknameReq postNicknameReq) throws BaseException {
        boolean existence = authService.existsByNickname(postNicknameReq.getNickname());
        if(existence) throw new BaseException(EXIST_NICKNAME);
    }

    //마이페이지 조회


    //사용자 프로필 수정


    //회원 탈퇴


    //로그아웃


    // AccessToken 재발급


}
