package com.umc.place.user.OAuth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.place.user.OAuth2.dto.KakaoTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class KakaoAuthService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    //@Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    //private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.OAuth2.kakao.url.token}")
    private String KAKAO_TOKEN_REQUEST_URL;

    @Value("${spring.OAuth2.kakao.url.profile}")
    private String KAKAO_USERINFO_REQUEST_URL;

    //인가코드로 카카오 토큰 발급받기
    public KakaoTokenResponse getKakaoToken (String code) throws JsonProcessingException {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", kakaoClientId);
        params.put("redirect_uri", kakaoRedirectUri);
        params.put("grant_type", "authorization_code");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(KAKAO_TOKEN_REQUEST_URL,
                params, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoTokenResponse kakaoOAuthToken = objectMapper.readValue(response.getBody(), KakaoTokenResponse.class);
        return kakaoOAuthToken;
    }

    //카카오 토큰으로 사용자 정보(식별자) 가져오기
    public String getKakaoIdentifier (KakaoTokenResponse kakaoToken) throws JsonProcessingException {

        //header에 accessToken 담기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());

        //user id 가져오기
        String identifier = jsonNode.get("id").asText();

        return identifier;
    }

}

