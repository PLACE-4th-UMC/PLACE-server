package com.umc.place.story.repository;

import com.umc.place.story.entity.Story;
import com.umc.place.story.entity.StoryHistory;
import com.umc.place.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryHistoryRepository extends JpaRepository<StoryHistory, Long> {
    List<StoryHistory> findFirst4ByUserOrderByLastModifiedDateDesc(User user);

    Boolean existsByUserAndStory(User user, Story story);

    Optional<StoryHistory> findByUserAndStory(User user, Story story);
}
