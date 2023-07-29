package com.umc.place.story.repository;

import com.umc.place.story.entity.Story;
import com.umc.place.story.entity.StoryLike;
import com.umc.place.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryLikeRepository extends JpaRepository<StoryLike, Long> {
    Boolean existsByUserAndStory(User user, Story story);
    List<StoryLike> findByUser(User user);
}
