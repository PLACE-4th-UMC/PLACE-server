package com.umc.place.exhibition.repository;


import com.umc.place.exhibition.entity.Exhibition;
import com.umc.place.exhibition.entity.ExhibitionLike;
import com.umc.place.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionLikeRepository extends JpaRepository<ExhibitionLike, Long> {
    ExhibitionLike findByExhibitionAndUser(Exhibition exhibition, User user);
}
