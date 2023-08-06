package com.umc.place.exhibition.repository;

import com.umc.place.exhibition.entity.Category;
import com.umc.place.exhibition.entity.Exhibition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    Page<Exhibition> findByCategory(Category category, Pageable pageable); // 카테고리 기반 전체 조회(페이징)
    Page<Exhibition> findAll(Pageable pageable); // 전체 조회(페이징)
    boolean existsByCategory(Category category);

    @Query("select case when count(e) > 0 then true else false end from Exhibition e where e.location like %:location%")
    boolean existsByLocationLike(@Param("location") String location);

    @Modifying
    @Query("update Exhibition e set e.viewCount = e.viewCount + 1 where e.exhibitionIdx = :exhibitionIdx")
    int updateView(@Param("exhibitionIdx") Long exhibitionIdx);

    @Query("select e from Exhibition e where e.location like %:location%")
    Page<Exhibition> findByLocationLike(@Param("location") String location, Pageable pageable);
}
