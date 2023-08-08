package com.umc.place.user.service;

import com.umc.place.common.BaseException;
import com.umc.place.common.Constant;
import com.umc.place.story.entity.Story;
import com.umc.place.story.entity.StoryLike;
import com.umc.place.story.repository.StoryLikeRepository;
import com.umc.place.story.repository.StoryRepository;
import com.umc.place.user.dto.*;
import com.umc.place.user.entity.Provider;
import com.umc.place.user.entity.User;
import com.umc.place.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.umc.place.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor //생성자 자동 생성
public class UserService {

    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final AuthService authService;

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

    private PostUserRes signUpOrLogin(String identifier, Provider provider) throws BaseException {
        User user = userRepository.findByIdentifierAndProvider(identifier, provider);
        //기존 회원이 아닐 경우 회원가입
        if (user == null)
            user = signup(identifier, provider);
        //탈퇴한 회원일 경우
        if (user.getStatus().equals("inactive"))
            throw new BaseException(ALREADY_WITHDRAW_USER);
        //기존 회원이면 로그인 처리
        user.login(); //user status active로 바꾸기
        userRepository.save(user); //userRepository에 user 저장
        return authService.createToken(user); //user의 JWT access token, refresh token 반환
    }

    //첫 로그인 시 user 생성 및 저장 (가입 연도 추가하기 createdDate)
    public User signup(String identifier, Provider provider) {
        //userIdx는 자동 생성
        User newuser = User.builder()
                .identifier(identifier)
                .provider(provider)
                .build();
        return userRepository.save(newuser);
    }

    //회원 가입 후 사용자 정보 입력
    @Transactional(rollbackFor = Exception.class)
    public PostUserRes signup_UserInfo(String identifier, PostNewUserReq postNewUserReq) throws BaseException {
        try{
            //identifier로 해당 user 찾기
            User user = userRepository.findByIdentifierAndStatus(identifier, "active").orElseThrow(()->new BaseException(INVALID_IDENTIFIER));
            user.signup(postNewUserReq.getNickname(), postNewUserReq.getUserImg(), postNewUserReq.getBirthday(), postNewUserReq.getLocation(), postNewUserReq.getEmail());
            userRepository.save(user); //repository에 저장
            //access token, refresh token
            String accessToken = authService.createAccessToken(user.getIdentifier());
            String refreshToken = authService.createRefreshToken(user.getIdentifier());

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
        String identifier = authService.getIdentifier();
        User user = userRepository.findByIdentifierAndStatus(identifier, "active").orElseThrow(() -> new BaseException(INVALID_IDENTIFIER));
        int year = user.getCreatedDate().getYear();

        //내가 작성한 스토리 가져오기
        Optional <Story> storyList = storyRepository.findFirstByUserOrderByCreatedDateDesc(user);

        return new GetProfileRes(user.getUserImg(), user.getNickname(), "Hello, " + user.getNickname(), year, storyList);
    }

    //사용자 프로필 수정
    @Transactional
    public void modifyProfile(String identifier, PatchProfileReq patchProfileReq) throws BaseException {
        try{
            User user = userRepository.findByIdentifierAndStatus(identifier, "active").orElseThrow(() -> new BaseException(INVALID_IDENTIFIER));

            if(patchProfileReq.getNickname() != null)
                user.modifyNickname(patchProfileReq.getNickname());
            if(patchProfileReq.getUserImg() != null)
                user.modifyUserImg(patchProfileReq.getUserImg());
            if(patchProfileReq.getEmail() != null)
                user.modifyEmail(patchProfileReq.getEmail());
            if(patchProfileReq.getLocation() != null)
                user.modifyLocation(patchProfileReq.getLocation());

        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //회원 탈퇴
    @Transactional(rollbackFor = Exception.class)
    public void signout(String identifier) throws BaseException {
        try{
            User user = userRepository.findByIdentifierAndStatus(identifier, "active").orElseThrow(() -> new BaseException(INVALID_IDENTIFIER));
            userRepository.delete(user);
            authService.deleteToken(identifier);
            String token = user.getAccessToken();
            authService.registerBlackList(token, Constant.INACTIVE);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //로그아웃
    @Transactional
    public void logout (String identifier) throws BaseException {
        try{
            User user = userRepository.findByIdentifierAndStatus(identifier, "active").orElseThrow(() -> new BaseException(INVALID_IDENTIFIER));
            authService.deleteToken(identifier);
            String token = user.getAccessToken();
            authService.registerBlackList(token, Constant.LOGOUT);
            user.logout();
        } catch (BaseException e){
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // AccessToken 재발급
    @Transactional
    public PostUserRes reissueToken (PostTokenReq postTokenReq) throws BaseException {
        User user = userRepository.findByIdentifierAndStatus(postTokenReq.getIdentifier(),"active")
                .orElseThrow(() -> new BaseException(INVALID_IDENTIFIER));
        authService.validateRefreshToken(user.getIdentifier(), postTokenReq.getRefreshToken());
        return authService.createToken(user);
    }


}
