package com.umc.place.user.service;

import com.umc.place.common.BaseException;
import com.umc.place.common.Constant;
import com.umc.place.user.dto.PostUserRes;
import com.umc.place.user.entity.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.util.Date;

import static com.umc.place.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final int accessTokenExpiryDate = 604800000;
    private final int refreshTokenExpiryDate = 604800000;

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${auth.key}")
    private String key;
    String CLAIM_NAME = "userIdx";
    Long ADMIN_USERIDX = 0L;
    String REQUEST_HEADER_NAME = "Authorization";
    public static final String TOKEN_REGEX = "^Bearer( )*";
    public static final String TOKEN_REPLACEMENT = "";

    private final RepositoryService repositoryService;


    /**
     * userIdx 선택
     * @return 있으면 id, 없으면 ADMIN_USERIDX
     */
    public Long getUserIdxOptional() throws BaseException{
        String token = getToken();
        Long userIdx;
        if(token!=null) userIdx = getClaims(token).getBody().get(CLAIM_NAME, Long.class);
        else userIdx = ADMIN_USERIDX;
        return userIdx;
    }

    /**
     * userIdx 필수
     * @return 있으면 id, 없으면 EXCEPTION
     */
    public Long getUserIdx() throws BaseException{
        String token = getToken();
        if(token==null) throw new BaseException(NULL_TOKEN);
        return getClaims(token).getBody().get(CLAIM_NAME, Long.class);
    }

    // 토큰 추출
    private String getToken() throws BaseException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader(REQUEST_HEADER_NAME);
        if (token == null) return null;
        if(redisTemplate.opsForValue().get(token)!=null) throw new BaseException(INVALID_TOKEN);
        return token;
    }

    public Jws<Claims> getClaims(String token) throws BaseException{
        Jws<Claims> claims = null;
        token = token.replaceAll(TOKEN_REGEX, TOKEN_REPLACEMENT);
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new BaseException(EXPIRED_TOKEN);
        } catch (MalformedJwtException malformedJwtException) {
            throw new BaseException(MALFORMED_TOKEN);
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new BaseException(UNSUPPORTED_TOKEN);
        } catch (Exception e) {
            throw new BaseException(INVALID_TOKEN);
        }
        return claims;
    }

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
        User user = repositoryService.findUserByIdAndStatus(userIdx, "active").orElseThrow(()->new BaseException(INVALID_USER_IDX));
        String token = user.getAccessToken();
        registerBlackList(token, Constant.LOGOUT);
    }

    // 회원 탈퇴
    public void signout(Long userIdx) throws BaseException {
        deleteToken(userIdx);
        User user = repositoryService.findUserByIdAndStatus(userIdx, "active").orElseThrow(()->new BaseException(INVALID_USER_IDX));
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
