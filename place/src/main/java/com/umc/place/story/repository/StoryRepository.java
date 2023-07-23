package com.umc.place.story.repository;

import com.umc.place.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
}
