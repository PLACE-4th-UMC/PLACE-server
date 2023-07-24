package com.umc.place.exhibitionLike.entity;

import com.umc.place.common.BaseEntity;
import com.umc.place.exhibition.entity.Exhibition;
import com.umc.place.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@Table(name = "exhibitionLikes")
public class ExhibitionLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exhibitionLikeIdx;

    @ManyToOne
    @JoinColumn(name = "exhibitionIdx")
    private Exhibition exhibition;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private User user;
}
