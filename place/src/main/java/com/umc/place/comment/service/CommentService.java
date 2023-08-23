package com.umc.place.comment.service;

import com.umc.place.comment.dto.CommentUploadReqDto;
import com.umc.place.comment.dto.CommentUploadResDto;
import com.umc.place.comment.entity.Comment;
import com.umc.place.comment.repository.CommentRepository;
import com.umc.place.common.BaseException;
import com.umc.place.story.entity.Story;
import com.umc.place.story.repository.StoryRepository;
import com.umc.place.user.entity.User;
import com.umc.place.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.umc.place.common.BaseResponseStatus.*;
import static com.umc.place.common.Constant.INACTIVE;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentUploadResDto uploadComment(Long storyIdx, CommentUploadReqDto reqDto, Long userIdx) throws BaseException {
        try {
            Story story = storyRepository.findById(storyIdx).orElseThrow(() -> new BaseException(INVALID_STORY_IDX));
            User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            Comment comment = Comment.builder()
                    .user(user)
                    .story(story)
                    .content(reqDto.getContent())
                    .build();
            commentRepository.save(comment);

            return new CommentUploadResDto(story.getComments());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Void deleteComment(Long storyIdx, Long commentIdx, Long ownerIdx) throws BaseException {
        try {
            Comment comment = commentRepository.findById(commentIdx).orElseThrow(() -> new BaseException(INVALID_COMMENT_IDX));
            User owner = userRepository.findById(ownerIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Story story = storyRepository.findById(storyIdx).orElseThrow(() -> new BaseException(INVALID_STORY_IDX));

            if (!comment.getUser().equals(owner)) { // 현재 로그인한 사람이 comment의 주인이 아닐때
                throw new BaseException(NOT_OWNER);
            }

            comment.setStatus(INACTIVE);
            story.getComments().remove(comment); // 양방향 연관관계

            return null;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
