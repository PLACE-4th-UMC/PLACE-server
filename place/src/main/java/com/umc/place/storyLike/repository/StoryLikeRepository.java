package com.umc.place.storyLike.repository;

import com.umc.place.story.entity.Story;
import com.umc.place.storyLike.entity.StoryLike;
import com.umc.place.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryLikeRepository extends JpaRepository<StoryLike, Long> {
    Boolean existsByUserAndStory(User user, Story story);
}
