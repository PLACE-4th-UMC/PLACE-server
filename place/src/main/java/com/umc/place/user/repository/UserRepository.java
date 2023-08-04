package com.umc.place.user.repository;


import com.umc.place.user.entity.Provider;
import com.umc.place.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname(String nickname);
    Optional<User> findByUserIdxAndStatus(Long userIdx, String status);
    User findByIdentifierAndProvider(String identifier, Provider provider);
    Optional<User> findByEmailAndProviderAndStatus(String email, Provider provider, String status);


}
