package com.umc.place.magazine.service;

import com.umc.place.common.BaseException;
import com.umc.place.exhibition.entity.ExhibitionLike;
import com.umc.place.exhibition.repository.ExhibitionLikeRepository;
import com.umc.place.magazine.dto.GetMagazineDetailRes;
import com.umc.place.story.entity.StoryLike;
import com.umc.place.story.repository.StoryLikeRepository;
import com.umc.place.user.entity.User;
import com.umc.place.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.umc.place.common.BaseResponseStatus.INVALID_USER_IDX;


@Service
@RequiredArgsConstructor
public class MagazineService {


    private final UserRepository userRepository;
    private final ExhibitionLikeRepository exhibitionLikeRepository;
    private final StoryLikeRepository storyLikeRepository;
    public GetMagazineDetailRes getMagazineDetail(Long userIdx) throws BaseException {

        User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        List<StoryLike> storyLike = storyLikeRepository.findByUser(user);
        List<ExhibitionLike> exhibitionLike = exhibitionLikeRepository.findByUser(user);

        List<GetMagazineDetailRes.LikeList> storyList = storyLike.stream()  //story 좋아요 가져오기
                .map(story -> new GetMagazineDetailRes.LikeList(story.getStory().getStoryImg(), story.getStory().getExhibition().getLocation()))
                .collect(Collectors.toList());


        List<GetMagazineDetailRes.LikeList> exhibitionLikeList = exhibitionLike.stream()//전시회 좋아요 가져오기
                .map(exhibition -> new GetMagazineDetailRes.LikeList(exhibition.getExhibition().getExhibitionImg(), exhibition.getExhibition().getLocation()))
                .collect(Collectors.toList());



        storyList.addAll(exhibitionLikeList);   //스토리와 전시회 좋아요 한 것 모두 합치기


        return new GetMagazineDetailRes(storyList);
    }
}
