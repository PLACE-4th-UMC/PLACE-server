package com.umc.place.user.entity;

import com.umc.place.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false, length = 50)
    private String email;

    @Column
    private String userImg;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(nullable = false)
    private Date birthday;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    @Builder
    public User(Long userIdx, Provider provider) {
        this.userIdx = userIdx;
        this.provider = provider;
    }

    public void signup(String nickname, String userImg, Date birthday, String location){
        this.nickname = nickname;
        this.userImg = userImg;
        this.birthday = birthday;
        this.location = location;
    }

    public void storeSignUp(String nickname, String userImg, Provider provider) {
        this.nickname = nickname;
        this.userImg = userImg;
        this.provider = provider;
    }

    //탈퇴하기
    public void signout() {
        this.setNickname("알 수 없음");
        this.setProfileImg(null);
        this.setProvider(Provider.ANONYMOUS);
        this.setStatus("inactive");
    }

    public void setProfileImg(String userImg) {
        this.userImg = userImg;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setProvider(Provider provider){
        this.provider = provider;
    }

    public void logout() {
        this.setStatus("logout");
    }
    public void login() {
        this.setStatus("active");
    }


}
