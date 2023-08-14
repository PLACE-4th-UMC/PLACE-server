package com.umc.place.user.OAuth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.place.user.OAuth2.dto.NaverTokenResponse;
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
public class NaverAuthService {
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${spring.OAuth2.naver.url.token}")
    private String NAVER_TOKEN_REQUEST_URL;

    @Value("${spring.OAuth2.naver.url.profile}")
    private String NAVER_USERINFO_REQUEST_URL;

    //인가코드로 토큰 발급받기
    public NaverTokenResponse getNaverToken (String code, String state) throws JsonProcessingException {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "code");
        params.put("client_id", naverClientId);
        params.put("client_secret", naverClientSecret);
        params.put("redirect_uri", naverRedirectUri);
        params.put("code", code);
        params.put("state", state);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(NAVER_TOKEN_REQUEST_URL,
                params, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        NaverTokenResponse naverOAuthToken = objectMapper.readValue(response.getBody(), NaverTokenResponse.class);
        return naverOAuthToken;
    }

    //네이버 토큰으로 사용자 정보(식별자) 가져오기
    public String getNaverIdentifier (NaverTokenResponse naverToken) throws JsonProcessingException {

        //header에 accessToken 담기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + naverToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(NAVER_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());

        //user id 가져오기
        String identifier = jsonNode.get("id").asText();

        return identifier;
    }


}