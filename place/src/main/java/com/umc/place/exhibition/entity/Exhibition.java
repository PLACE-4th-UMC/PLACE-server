package com.umc.place.exhibition.entity;

import com.umc.place.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Exhibition extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exhibitionIdx;

    @Column(nullable = false, length = 30)
    private String exhibitionName;

    @Column
    private String exhibitionImg;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false, length = 200)
    private String operatingTime;

    @Column(nullable = false, length = 200)
    private String location;

    @Column
    private Integer fee;

    @Column(nullable = false)
    private String artist;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;
}
