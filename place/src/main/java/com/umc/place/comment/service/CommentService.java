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
}
