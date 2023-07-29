package com.umc.place.story.service;

import com.umc.place.comment.dto.CommentResDto;
import com.umc.place.common.BaseException;
import com.umc.place.story.dto.StoryDetailResponseDto;
import com.umc.place.story.entity.Story;
import com.umc.place.story.repository.StoryLikeRepository;
import com.umc.place.story.repository.StoryRepository;
import com.umc.place.user.entity.User;
import com.umc.place.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.umc.place.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;
    private final StoryLikeRepository storyLikeRepository;
    private final UserRepository userRepository;

    public StoryDetailResponseDto getStoryDetail(Long storyIdx, Long userId) throws BaseException {
        try {
            Story findStoryById
                    = storyRepository.findById(storyIdx).orElseThrow(() -> new BaseException(INVALID_STORY_IDX));

            User findUserById = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            List<CommentResDto> commentDtos = findStoryById.getComments().stream()
                    .map(comment -> new CommentResDto(comment))
                    .collect(Collectors.toList());

            return StoryDetailResponseDto.builder()
                    .exhibitionImg(findStoryById.getExhibition().getExhibitionImg())
                    .exhibitionAddress(findStoryById.getExhibition().getLocation())
                    .exhibitionName(findStoryById.getExhibition().getExhibitionName())
                    .storyOwnerImg(findUserById.getUserImg())
                    .comments(commentDtos)
                    .isLiked(storyLikeRepository.existsByUserAndStory(findUserById, findStoryById))
                    .build();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
