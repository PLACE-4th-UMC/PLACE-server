package com.umc.place.comment.entity;

import com.umc.place.common.BaseEntity;
import com.umc.place.story.entity.Story;
import com.umc.place.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private User user;

    @ManyToOne
    @JoinColumn(name = "storyIdx")
    private Story story;

    @Column(nullable = false, length = 300)
    private String content;

    @Builder
    public Comment(User user, Story story, String content) {
        this.user = user;
        this.story = story;
        story.getComments().add(this); // 양방향 연관관계 메서드
        this.content = content;
    }
}
