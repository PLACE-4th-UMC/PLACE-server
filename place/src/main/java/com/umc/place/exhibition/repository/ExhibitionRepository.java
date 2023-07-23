package com.umc.place.exhibition.repository;

import com.umc.place.exhibition.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
}
