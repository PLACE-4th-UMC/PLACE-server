package com.umc.place.story.entity;

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
public class Story extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storyIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exhibitionIdx")
    private Exhibition exhibition;

    @Column
    private String storyImg;
}
