package com.umc.place.user.service;

import com.umc.place.common.BaseException;
import com.umc.place.common.Constant;
import com.umc.place.user.dto.PostUserRes;
import com.umc.place.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

import static com.umc.place.common.BaseResponseStatus.INVALID_TOKEN;
import static com.umc.place.common.BaseResponseStatus.INVALID_USER_IDX;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final int accessTokenExpiryDate = 604800000;
    private final int refreshTokenExpiryDate = 604800000;

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${auth.key}")
    private String key;

    public static final String TOKEN_REGEX = "^Bearer( )*";
    public static final String TOKEN_REPLACEMENT = "";

    private final AuthService authService;


    //user id로 JWT 토큰 생성
    public PostUserRes createToken(User user){
        String accessToken = createAccessToken(user.getUserIdx());
        String refreshToken = createRefreshToken(user.getUserIdx());
        return new PostUserRes(accessToken, refreshToken);
    }

    public String createRefreshToken(Long userIdx){
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + refreshTokenExpiryDate))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        redisTemplate.opsForValue().set(String.valueOf(userIdx), refreshToken, Duration.ofMillis(refreshTokenExpiryDate));
        return refreshToken;
    }
    public String createAccessToken(Long userIdx){
        Date now = new Date();
        String accessToken = Jwts.builder()
                .claim("userIdx", userIdx)
                .setSubject(userIdx.toString())
                .setExpiration(new Date(now.getTime() + accessTokenExpiryDate))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        return accessToken;
    }

    //토큰 재발급 시 사용
    public String validateRefreshToken(Long userIdx, String refreshTokenReq) throws BaseException {
        String refreshToken = (String) redisTemplate.opsForValue().get(String.valueOf(userIdx));
        if(!refreshToken.equals(refreshTokenReq)) throw new BaseException(INVALID_TOKEN);
        return refreshToken;
    }

    // 회원 로그아웃
    public void logout(Long userIdx) throws BaseException {
        deleteToken(userIdx);
        User user = authService.findUserByIdAndStatus(userIdx, "active").orElseThrow(()->new BaseException(INVALID_USER_IDX));
        String token = user.getAccessToken();
        registerBlackList(token, Constant.LOGOUT);
    }

    // 회원 탈퇴
    public void signout(Long userIdx) throws BaseException {
        deleteToken(userIdx);
        User user = authService.findUserByIdAndStatus(userIdx, "active").orElseThrow(()->new BaseException(INVALID_USER_IDX));
        String token = user.getAccessToken();
        registerBlackList(token, Constant.INACTIVE);
    }

    // refreshToken 삭제
    public void deleteToken(Long userIdx) {
        String key = String.valueOf(userIdx);
        if(redisTemplate.opsForValue().get(key)!=null) redisTemplate.delete(key);
    }

    // 유효한 토큰(Bearer) blacklist로 등록
    private void registerBlackList(String token, String status) {
        token = token.replaceAll(TOKEN_REGEX, TOKEN_REPLACEMENT);
        Date AccessTokenExpiration = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getExpiration();
        long now = (new Date()).getTime();

        Long expiration = AccessTokenExpiration.getTime() - now;
        redisTemplate.opsForValue().set(token, status, Duration.ofMillis(expiration));
    }
}
