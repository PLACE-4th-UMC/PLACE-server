package com.umc.place.user.service;

import com.umc.place.common.BaseException;
import com.umc.place.user.dto.PostUserRes;
import com.umc.place.user.entity.User;
import com.umc.place.user.repository.UserRepository;
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
import static com.umc.place.common.Constant.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final int accessTokenExpiryDate = 604800000;
    private final int refreshTokenExpiryDate = 604800000;

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${auth.key}")
    private String key;
    String CLAIM_NAME = "identifier";
    String REQUEST_HEADER_NAME = "Authorization";
    public static final String TOKEN_REGEX = "^Bearer( )*";
    public static final String TOKEN_REPLACEMENT = "";

    private final UserRepository userRepository;

    // 토큰 추출하기
    public String getToken() throws BaseException {
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

    //identifier 가져오기
    public String getIdentifier() throws BaseException{
        String token = getToken();
        if(token==null) throw new BaseException(NULL_TOKEN);
        return getClaims(token).getBody().get(CLAIM_NAME, String.class);
    }

    //userIdx 가져오기
    public Long getUserIdx() throws BaseException {
        String identifier = getIdentifier();
        User user = userRepository.findByIdentifierAndStatus(identifier, ACTIVE).orElseThrow(() -> new BaseException(INVALID_IDENTIFIER));
        Long userIdx = user.getUserIdx();
        return userIdx;
    }

    //소셜로그인 시 받아온 identifier로 JWT 토큰 생성
    public PostUserRes createToken (User user) {
        String accessToken = createAccessToken(user.getIdentifier());
        String refreshToken = createRefreshToken(user.getIdentifier());
        return new PostUserRes(accessToken, refreshToken);
    }

    public String createAccessToken (String identifier) {
        Date now = new Date();
        String accessToken = Jwts.builder()
                .claim("identifier", identifier)
                .setSubject(identifier)
                .setExpiration(new Date(now.getTime() + accessTokenExpiryDate))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        return accessToken;
    }

    public String createRefreshToken (String identifier) {
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + refreshTokenExpiryDate))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        redisTemplate.opsForValue().set(String.valueOf(identifier), refreshToken, Duration.ofMillis(refreshTokenExpiryDate));
        return refreshToken;
    }

    //토큰 재발급 시 사용
    public String validateRefreshToken (String identifier, String refreshTokenReq) throws BaseException {
        String refreshToken = redisTemplate.opsForValue().get(String.valueOf(identifier));

        if(!refreshToken.equals(refreshTokenReq)) throw new BaseException(INVALID_TOKEN);
        return refreshToken;
    }

    // refreshToken 삭제
    public void deleteToken (String identifier) {
        String key = String.valueOf(identifier);
        if(redisTemplate.opsForValue().get(key)!=null) redisTemplate.delete(key);
    }

    // 유효한 토큰(Bearer) blacklist로 등록
    public void registerBlackList (String token, String status) {
        token = token.replaceAll(TOKEN_REGEX, TOKEN_REPLACEMENT);
        Date AccessTokenExpiration = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getExpiration();
        long now = (new Date()).getTime();

        Long expiration = AccessTokenExpiration.getTime() - now;
        redisTemplate.opsForValue().set(token, status, Duration.ofMillis(expiration));
    }

}
