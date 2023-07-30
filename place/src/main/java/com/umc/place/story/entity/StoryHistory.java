package com.umc.place.story.entity;

import com.umc.place.common.BaseEntity;
import com.umc.place.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class StoryHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storyHistoryIdx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userIdx")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "storyIdx")
    private Story story;

    @Builder
    public StoryHistory(User user, Story story) {
        this.user = user;
        this.story = story;
    }
}
