package com.umc.place.story.service;

import com.umc.place.comment.dto.CommentResDto;
import com.umc.place.comment.repository.CommentRepository;
import com.umc.place.common.BaseException;
import com.umc.place.exhibition.dto.SearchExhibitionsByNameResDto;
import com.umc.place.exhibition.entity.Exhibition;
import com.umc.place.exhibition.repository.ExhibitionRepository;
import com.umc.place.story.dto.StoryDetailResponseDto;
import com.umc.place.story.dto.StoryUploadRequestDto;
import com.umc.place.story.dto.StoryUploadResponseDto;
import com.umc.place.story.entity.Story;
import com.umc.place.story.entity.StoryHistory;
import com.umc.place.story.entity.StoryLike;
import com.umc.place.story.repository.StoryHistoryRepository;
import com.umc.place.story.repository.StoryLikeRepository;
import com.umc.place.story.repository.StoryRepository;
import com.umc.place.user.entity.User;
import com.umc.place.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.umc.place.common.BaseResponseStatus.*;
import static com.umc.place.common.Constant.ACTIVE;
import static com.umc.place.common.Constant.INACTIVE;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;
    private final StoryLikeRepository storyLikeRepository;
    private final StoryHistoryRepository storyHistoryRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public StoryDetailResponseDto getStoryDetail(Long storyIdx, Long userId) throws BaseException {
        try {
            Story findStoryById
                    = storyRepository.findById(storyIdx).orElseThrow(() -> new BaseException(INVALID_STORY_IDX));

            User findUserById = null;
            // 회원 - record history
            if (userId != null) {
                findUserById = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

                // update history
                Boolean existsByUserAndStory
                        = storyHistoryRepository.existsByUserAndStory(findUserById, findStoryById);
                if (existsByUserAndStory) {
                    StoryHistory history = storyHistoryRepository.findByUserAndStory(findUserById, findStoryById)
                            .orElseThrow(() -> new BaseException(INVALID_STORY_IDX));
                    history.setLastModifiedDate(LocalDateTime.now());
                } else {
                    StoryHistory history = StoryHistory.builder()
                            .story(findStoryById)
                            .user(findUserById)
                            .build();
                    storyHistoryRepository.save(history);
                }
            }

            List<CommentResDto> commentDtos = findStoryById.getComments().stream()
                    .map(comment -> new CommentResDto(comment))
                    .collect(Collectors.toList());

            return StoryDetailResponseDto.builder()
                    .storyImg(findStoryById.getStoryImg())
                    .exhibitionAddress(findStoryById.getExhibition().getLocation())
                    .exhibitionName(findStoryById.getExhibition().getExhibitionName())
                    .storyOwnerImg(findStoryById.getUser().getUserImg())
                    .comments(commentDtos)
                    .isLiked(
                            (userId == null) ? null : storyLikeRepository.existsByUserAndStory(findUserById, findStoryById))
                    .build();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public SearchExhibitionsByNameResDto searchExhibitionByName(String searchWord, Pageable page) throws BaseException {
        try {
            searchWord = searchWord.trim();
            Page<Exhibition> searchedExhibitions
                    = exhibitionRepository.findByExhibitionNameContainingOrderByExhibitionName(searchWord, page);
            return SearchExhibitionsByNameResDto.builder()
                    .exhibitions(searchedExhibitions)
                    .build();
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public StoryUploadResponseDto uploadStory(StoryUploadRequestDto storyUploadRequestDto, Long userId, String storyImgUrl) throws BaseException {
        try {
            Exhibition exhibition = exhibitionRepository.findById(storyUploadRequestDto.getExhibitionIdx())
                    .orElseThrow(() -> new BaseException(INVALID_EXHIBITION_IDX));
            User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            Story newStory = Story.builder()
                    .user(user)
                    .exhibition(exhibition)
                    .storyImg(storyImgUrl)
                    .build();

            Story savedStory = storyRepository.save(newStory);

            List<StoryHistory> dateDesc = storyHistoryRepository.findFirst4ByUserOrderByLastModifiedDateDesc(user);

            return StoryUploadResponseDto.builder()
                    .latestStoryImg(savedStory.getStoryImg())
                    .latestStoryName(savedStory.getExhibition().getExhibitionName())
                    .latestStoryLocation(savedStory.getExhibition().getLocation())
                    .recentStories(dateDesc)
                    .build();

        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);

        }
    }

    public StoryUploadResponseDto getStoryView(Long userId) throws BaseException {
        try {
            User findUser = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            List<StoryHistory> storyHistoryList
                    = storyHistoryRepository.findFirst4ByUserOrderByLastModifiedDateDesc(findUser);
            Optional<Story> latestStory = storyRepository
                    .findFirstByUserOrderByCreatedDateDesc(findUser);

            if (latestStory.isPresent()) {
                return StoryUploadResponseDto.builder()
                        .recentStories(storyHistoryList)
                        .latestStoryImg(latestStory.get().getStoryImg())
                        .latestStoryName(latestStory.get().getExhibition().getExhibitionName())
                        .latestStoryLocation(latestStory.get().getExhibition().getLocation())
                        .build();
            } else {
                return StoryUploadResponseDto.builder()
                        .recentStories(storyHistoryList)
                        .build();
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void likeStory(Long storyIdx, Long userIdx) throws BaseException {
        try {
            User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Story story = storyRepository.findById(storyIdx).orElseThrow(() -> new BaseException(INVALID_STORY_IDX));

            StoryLike storyLike = storyLikeRepository.findByUserAndStory(user, story);
            if (storyLike == null) {
                StoryLike newLike = StoryLike.builder()
                        .user(user)
                        .story(story)
                        .build();
                storyLikeRepository.save(newLike);
            } else {
                if (storyLike.getStatus().equals(ACTIVE)) {
                    storyLike.setStatus(INACTIVE);
                } else {
                    storyLike.getStatus().equals(ACTIVE);
                }
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
