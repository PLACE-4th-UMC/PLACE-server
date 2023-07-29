package com.umc.place.exhibition.service;

import com.umc.place.common.BaseException;
import com.umc.place.exhibition.dto.GetExhibitionDetailRes;
import com.umc.place.exhibition.entity.Exhibition;
import com.umc.place.exhibition.entity.ExhibitionLike;
import com.umc.place.exhibition.repository.ExhibitionRepository;
import com.umc.place.exhibition.repository.ExhibitionLikeRepository;
import com.umc.place.story.entity.Story;
import com.umc.place.story.repository.StoryRepository;
import com.umc.place.story.repository.StoryLikeRepository;
import com.umc.place.user.entity.User;
import com.umc.place.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.umc.place.common.BaseResponseStatus.*;
import static com.umc.place.common.Constant.ACTIVE;
import static com.umc.place.common.Constant.INACTIVE;

@Service
@RequiredArgsConstructor
public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    private final StoryRepository storyRepository;
    private final StoryLikeRepository storyLikeRepository;
    private final UserRepository userRepository;
    private final ExhibitionLikeRepository exhibitionLikeRepository;

    /**
     * 전시회 상세 조회
     * @param exhibitionIdx
     * @return GetExhibitionDetailRes
     * @throws BaseException
     */
    public GetExhibitionDetailRes getExhibitionDetail(Long exhibitionIdx, Long userIdx) throws BaseException{
        try {
            Exhibition exhibition = exhibitionRepository.findById(exhibitionIdx).orElseThrow(() -> new BaseException(INVALID_EXHIBITION_IDX));

            List<GetExhibitionDetailRes.Story> storyList;
            if (userRepository.existsById(userIdx)) {
                User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
                //List<GetExhibitionDetailRes.Story> storyList = getStoryList(exhibition, user);
                storyList = getStoryList(exhibition, user);
            } else {
                storyList = getStoryListAnonymous(exhibition);
            }

            // TODO: S3 설정 완료 후 이미지 가져오는 부분 수정 필요
            return new GetExhibitionDetailRes(exhibition.getExhibitionName(), exhibition.getStartDate(), exhibition.getEndDate(),
                    exhibition.getExhibitionImg(), exhibition.getOperatingTime(), exhibition.getLocation(), exhibition.getFee(), exhibition.getArtist(), storyList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    // 회원) 스토리 조회
    private List<GetExhibitionDetailRes.Story> getStoryList(Exhibition exhibition, User user) {
        return storyRepository.findFirst2ByExhibition(exhibition).stream()
                .map(story -> new GetExhibitionDetailRes.Story(story.getStoryIdx(), story.getStoryImg(), story.getUser().getNickname(),
                        story.getUser().getUserImg(), getLike(user, story))).collect(Collectors.toList());
    }

    private Boolean getLike(User user, Story story) {
        // 좋아요 눌렀으면 True 아니면 False
        return storyLikeRepository.existsByUserAndStory(user, story);
    }

    // 비회원) 스토리 조회
    private List<GetExhibitionDetailRes.Story> getStoryListAnonymous(Exhibition exhibition) {
        return storyRepository.findFirst2ByExhibition(exhibition).stream()
                .map(story -> new GetExhibitionDetailRes.Story(story.getStoryIdx(), story.getStoryImg(), story.getUser().getNickname(),
                        story.getUser().getUserImg(), Boolean.FALSE)).collect(Collectors.toList());
    }

    /**
     * 전시회 좋아요 누르기
     * @param exhibitionIdx
     * @param userIdx
     * @throws BaseException
     */
    @Transactional(rollbackFor = Exception.class)
    public void likeExhibition(Long exhibitionIdx, Long userIdx) throws BaseException {
        try {
            Exhibition exhibition = exhibitionRepository.findById(exhibitionIdx).orElseThrow(() -> new BaseException(INVALID_EXHIBITION_IDX));
            // TODO: 비회원 예외 처리
            User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            ExhibitionLike exhibitionLike = exhibitionLikeRepository.findByExhibitionAndUser(exhibition, user);
            if (exhibitionLike == null) {
                exhibitionLike = ExhibitionLike.builder()
                        .exhibition(exhibition)
                        .user(user)
                        .build();
            } else {
                if(exhibitionLike.getStatus().equals(ACTIVE)) exhibitionLike.setStatus(INACTIVE);
                else exhibitionLike.setStatus(ACTIVE);
            }
            exhibitionLikeRepository.save(exhibitionLike);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
