package com.umc.place.storyLike.entity;

import com.umc.place.common.BaseEntity;
import com.umc.place.story.entity.Story;
import com.umc.place.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@Table(name = "storyLikes")
public class StoryLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storyLikeIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private User user;

    @ManyToOne
    @JoinColumn(name = "storyIdx")
    private Story story;
}
