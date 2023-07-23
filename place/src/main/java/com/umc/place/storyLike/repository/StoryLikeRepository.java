package com.umc.place.storyLike.repository;

import com.umc.place.storyLike.entity.StoryLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryLikeRepository extends JpaRepository<StoryLike, Long> {
}
