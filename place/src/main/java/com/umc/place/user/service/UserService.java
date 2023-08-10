package com.umc.place.user.service;

import com.umc.place.common.BaseException;
import com.umc.place.user.dto.*;
import com.umc.place.user.entity.Provider;
import com.umc.place.user.entity.User;
import com.umc.place.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.umc.place.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor //생성자 자동 생성
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    private PostUserRes signUpOrLogin(String identifier, Provider provider) throws BaseException {
        //Provider provider = Provider.KAKAO;
        User user = userRepository.findByIdentifierAndProvider(identifier, provider);
        //기존 회원이 아닐 경우 회원가입
        if (user == null)
            user = signup(identifier,provider); //provider 카카오로 설정
        //탈퇴한 회원일 경우
        if (user.getStatus().equals("inactive"))
            throw new BaseException(ALREADY_WITHDRAW_USER);
        //기존 회원이면 로그인 처리
        user.login(); //user status active로 바꾸기
        userRepository.save(user); //userRepository에 user 저장
        return authService.createToken(user); //user의 access token, refresh token 반환
    }

    //로그인
    public PostUserRes login(String identifier, String provider) throws BaseException{
        try{
            if(Provider.getProviderByName(provider) == null)
                throw new BaseException(INVALID_PROVIDER);
            return signUpOrLogin(identifier, Provider.getProviderByName(provider)); //access token, refresh token 반환
        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //첫 로그인 시 user 생성 및 저장 (가입 연도 추가하기 createdDate)
    public User signup(String identifier, Provider provider) {
        User newuser = User.builder()
                .identifier(identifier)
                .provider(provider)
                .build();
        return userRepository.save(newuser);
    }

    //회원 가입 후 사용자 정보 입력
    @Transactional(rollbackFor = Exception.class)
    public PostUserRes signup_UserInfo(Long userIdx, PostNewUserReq postNewUserReq) throws BaseException {
        try{
            //userIdx로 해당 user 찾기
            User user = userRepository.findByUserIdxAndStatus(userIdx, "active").orElseThrow(()->new BaseException(INVALID_USER_IDX));
            String accessToken = authService.createAccessToken(userIdx);
            String refreshToken = authService.createRefreshToken(userIdx);

            user.signup(postNewUserReq.getNickname(), postNewUserReq.getUserImg(), postNewUserReq.getBirthday(), postNewUserReq.getLocation(), postNewUserReq.getEmail());
            userRepository.save(user); //repository에 저장

            return new PostUserRes(accessToken, refreshToken);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //닉네임 중복 확인하기
    public void checkNickname(PostNicknameReq postNicknameReq) throws BaseException {
        boolean existence = userRepository.existsByNickname(postNicknameReq.getNickname());
        if(existence) throw new BaseException(EXIST_NICKNAME);
    }

    //마이페이지 조회
    public GetProfileRes getProfile() throws BaseException {
        Long userIdx = authService.getUserIdx();
        User user = userRepository.findByUserIdxAndStatus(userIdx, "active").orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        return new GetProfileRes(user.getUserImg(), user.getNickname(), "Hello, " + user.getNickname());
    }


    //사용자 프로필 수정


    //회원 탈퇴


    //로그아웃


    // AccessToken 재발급


}
