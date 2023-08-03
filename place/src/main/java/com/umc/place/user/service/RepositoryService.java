package com.umc.place.user.service;

import com.umc.place.user.entity.Provider;
import com.umc.place.user.entity.User;

import com.umc.place.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RepositoryService {
    //카카오 구글 네이버 회원 모두 관리하기 위해 userRepository에 이거로 저장
    private final UserRepository userRepository;
    @Autowired
    public RepositoryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User saveUser(User member) {
        return userRepository.save(member);
    }
    public User findUserByIdAndProvider(Long userIdx, Provider provider) {
        return userRepository.findByIdAndProvider(userIdx, provider);
    }

    public Optional<User> findUserByIdAndStatus(Long userIdx, String status) {
        return userRepository.findByUserIdxAndStatus(userIdx, status);
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}