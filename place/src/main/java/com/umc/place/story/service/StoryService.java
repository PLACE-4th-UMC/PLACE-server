package com.umc.place.story.service;

import com.umc.place.comment.dto.CommentResDto;
import com.umc.place.common.BaseException;
import com.umc.place.story.dto.StoryDetailResponseDto;
import com.umc.place.story.entity.Story;
import com.umc.place.story.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.umc.place.common.BaseResponseStatus.DATABASE_ERROR;
import static com.umc.place.common.BaseResponseStatus.INVALID_STORY_IDX;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;

    public StoryDetailResponseDto getStoryDetail(Long storyIdx) throws BaseException {
        try {
            Story findStoryById
                    = storyRepository.findById(storyIdx).orElseThrow(() -> new BaseException(INVALID_STORY_IDX));

            List<CommentResDto> commentDtos = findStoryById.getComments().stream()
                    .map(comment -> new CommentResDto(comment))
                    .collect(Collectors.toList());

            return StoryDetailResponseDto.builder()
                    .exhibitionAddress(findStoryById.getExhibition().getLocation())
                    .exhibitionName(findStoryById.getExhibition().getExhibitionName())
                    .comments(commentDtos)
                    .build();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
