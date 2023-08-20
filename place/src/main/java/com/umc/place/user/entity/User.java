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

    @Column(nullable = false)
    private String identifier;

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

    @Builder
    public User(String identifier, Provider provider) {
        this.identifier = identifier;
        this.provider = provider;
    }

    public void signup(String nickname, String userImg, Date birthday, String location, String email){
        this.nickname = nickname;
        this.userImg = userImg;
        this.birthday = birthday;
        this.location = location;
        this.email = email;
    }

    public void modifyNickname(String nickname) { this.nickname = nickname; }
    public void modifyUserImg(String userImg) {
        this.userImg = userImg;
    }
    public void modifyEmail(String email) {
        this.email = email;
    }
    public void modifyLocation(String location) {
        this.location = location;
    }

    public void logout() {
        this.setStatus("logout");
    }
    public void login() {
        this.setStatus("active");
    }

    public void setUserImg(String userImg){
        this.userImg = userImg;
    }

}
