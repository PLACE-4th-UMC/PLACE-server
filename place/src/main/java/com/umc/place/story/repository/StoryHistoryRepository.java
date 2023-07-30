package com.umc.place.story.repository;

import com.umc.place.story.entity.StoryHistory;
import com.umc.place.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryHistoryRepository extends JpaRepository<StoryHistory, Long> {
    List<StoryHistory> findFirst4ByUser(User user);
}
