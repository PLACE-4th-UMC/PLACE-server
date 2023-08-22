package com.umc.place.story.repository;

import com.umc.place.exhibition.entity.Exhibition;
import com.umc.place.story.entity.Story;
import com.umc.place.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findFirst2ByExhibition(Exhibition exhibition);

    Optional<Story> findFirstByUserOrderByCreatedDateDesc(User user);

    List<Story> findByUserOrderByCreatedDateDesc(User user);
    List<Story> findTop4ByOrderByCreatedDateDesc();
    List<Story> findTop4ByOrderByViewCountDesc();
    List<Story> findTop4ByOrderByLikeCountDesc();
}
