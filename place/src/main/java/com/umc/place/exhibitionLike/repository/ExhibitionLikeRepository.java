package com.umc.place.exhibitionLike.repository;


import com.umc.place.exhibitionLike.entity.ExhibitionLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionLikeRepository extends JpaRepository<ExhibitionLike, Long> {
}
