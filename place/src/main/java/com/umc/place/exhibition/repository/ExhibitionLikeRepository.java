package com.umc.place.exhibition.repository;


import com.umc.place.exhibition.entity.ExhibitionLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionLikeRepository extends JpaRepository<ExhibitionLike, Long> {
}
