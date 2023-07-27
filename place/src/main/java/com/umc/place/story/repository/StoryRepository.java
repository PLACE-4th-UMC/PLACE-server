package com.umc.place.story.repository;

import com.umc.place.exhibition.entity.Exhibition;
import com.umc.place.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findFirst2ByExhibition(Exhibition exhibition);
}
