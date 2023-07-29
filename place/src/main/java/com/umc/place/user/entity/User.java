package com.umc.place.user.entity;

import com.umc.place.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @Column
    private String userImg;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;
}
