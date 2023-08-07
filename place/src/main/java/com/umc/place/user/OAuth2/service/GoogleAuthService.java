package com.umc.place.user.OAuth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.place.user.OAuth2.dto.GoogleTokenResponse;
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
public class GoogleAuthService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${spring.OAuth2.google.url.token}")
    private String GOOGLE_TOKEN_REQUEST_URL;

    @Value("${spring.OAuth2.google.url.profile}")
    private String GOOGLE_USERINFO_REQUEST_URL;

    //인가코드로 카카오 토큰 발급받기
    public GoogleTokenResponse getGoogleToken (String authorizationCode) throws JsonProcessingException {
        Map<String, Object> params = new HashMap<>();
        params.put("code", authorizationCode);
        params.put("client_id", googleClientId);
        params.put("client_secret", googleClientSecret);
        params.put("redirect_uri", googleRedirectUri);
        params.put("grant_type", "authorization_code");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL,
                params, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleTokenResponse googleOAuthToken = objectMapper.readValue(response.getBody(), GoogleTokenResponse.class);
        return googleOAuthToken;
    }

    //구글 토큰으로 사용자 정보(식별자) 가져오기
    public String getGoogleIdentifier (GoogleTokenResponse googleToken) throws JsonProcessingException {

        //header에 accessToken 담기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());

        //user id 가져오기
        String identifier = jsonNode.get("sub").asText();

        return identifier;
    }

}
