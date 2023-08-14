package com.umc.place.exhibition.dto;

import com.umc.place.exhibition.entity.Exhibition;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class SearchExhibitionsByNameResDto {

    private List<SearchedExhibitionByName> searchedExhibitions = new ArrayList();

    @Builder
    public SearchExhibitionsByNameResDto(Page<Exhibition> exhibitions) {
        this.searchedExhibitions
                = exhibitions.stream()
                .map(exhibition -> new SearchedExhibitionByName(exhibition))
                .collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor
    public static class SearchedExhibitionByName {
        private Long exhibitionIdx;
        private String exhibitionName;

        @Builder
        public SearchedExhibitionByName(Exhibition exhibition) {
            this.exhibitionIdx = exhibition.getExhibitionIdx();
            this.exhibitionName = exhibition.getExhibitionName();
        }
    }
}
