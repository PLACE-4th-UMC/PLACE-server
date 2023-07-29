package com.umc.place.story.entity;

import com.umc.place.comment.entity.Comment;
import com.umc.place.common.BaseEntity;
import com.umc.place.exhibition.entity.Exhibition;
import com.umc.place.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Story extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storyIdx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userIdx")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "exhibitionIdx")
    private Exhibition exhibition;

    @Column
    private String storyImg;

    @OneToMany(mappedBy = "story") // 양방향 매핑
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "story") // 양방향 매핑
    private List<StoryLike> likes = new ArrayList<>();
}
