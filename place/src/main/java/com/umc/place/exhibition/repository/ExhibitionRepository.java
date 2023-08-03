package com.umc.place.exhibition.repository;

import com.umc.place.exhibition.entity.Category;
import com.umc.place.exhibition.entity.Exhibition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    Page<Exhibition> findByCategory(Category category, Pageable pageable); // 카테고리 기반 전체 조회(페이징)
    Page<Exhibition> findAll(Pageable pageable); // 전체 조회(페이징)
    Boolean existsByCategory(Category category);
}
