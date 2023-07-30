package com.umc.place.story.dto;

import com.umc.place.story.entity.StoryHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class StoryUploadResponseDto {
    private String latestStoryImg;
    private String latestStoryName;
    private String latestStoryLocation;

    private List<RecentStory> recentStories = new ArrayList<>();

    @Builder
    public StoryUploadResponseDto(String latestStoryImg, String latestStoryName,
                                  String latestStoryLocation, List<StoryHistory> recentStories) {
        this.latestStoryImg = latestStoryImg;
        this.latestStoryName = latestStoryName;
        this.latestStoryLocation = latestStoryLocation;
        this.recentStories = recentStories.stream()
                .map(story -> new RecentStory(story))
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    protected static class RecentStory {
        private String storyImg;
        private String storyOwnerName;
        private LocalDateTime storySharedDate;

        protected RecentStory(StoryHistory history) {
            this.storyImg = history.getStory().getStoryImg();
            this.storyOwnerName = history.getUser().getNickname();
            this.storySharedDate = history.getStory().getCreatedDate();
        }
    }
}
