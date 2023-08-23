package com.umc.place.user.OAuth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.place.user.OAuth2.dto.NaverTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
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

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String NAVER_TOKEN_REQUEST_URL;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String NAVER_USERINFO_REQUEST_URL;

    //인가코드로 토큰 발급받기
    public NaverTokenResponse getNaverToken (String code, String state) throws JsonProcessingException {
        // Set Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // Set parameter
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naverClientId);
        params.add("client_secret", naverClientSecret);
        params.add("code", code);
        params.add("state", state);

        RestTemplate restTemplate = new RestTemplate();

        // Set http entity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(NAVER_TOKEN_REQUEST_URL, HttpMethod.POST, request, String.class);

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
        String identifier = jsonNode.get("response").get("id").asText();

        return identifier;
    }


}