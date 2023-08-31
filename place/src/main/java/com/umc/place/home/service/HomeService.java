package com.umc.place.home.service;

import com.umc.place.common.BaseException;
import com.umc.place.exhibition.entity.Exhibition;
import com.umc.place.exhibition.repository.ExhibitionRepository;
import com.umc.place.home.dto.GetHomeUserLikeDetailRes;
import com.umc.place.home.dto.GetHomeUserRecentDetailRes;
import com.umc.place.home.dto.GetHomeUserViewDetailRes;
import com.umc.place.story.entity.Story;
import com.umc.place.story.repository.StoryLikeRepository;
import com.umc.place.story.repository.StoryRepository;
import com.umc.place.user.entity.User;
import com.umc.place.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.umc.place.common.BaseResponseStatus.INVALID_USER_IDX;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final StoryLikeRepository storyLikeRepository;

    public GetHomeUserRecentDetailRes getHomeUserRecentDetail() {   //로그인을 안하고 들어온경우
        List<Story> stories = storyRepository.findTop4ByOrderByCreatedDateDesc();
        List<GetHomeUserRecentDetailRes.UserRecentList> getUserRecentList = stories.stream()
                .map(userRecent -> new GetHomeUserRecentDetailRes.UserRecentList(userRecent.getExhibition().getExhibitionImg(),
                        userRecent.getUser().getUserImg(),
                        userRecent.getExhibition().getLocation(),
                        userRecent.getUser().getNickname(),
                        false,//하트를 보여주지않는다
                        false))// 로그인을 안한상태
                .collect(Collectors.toList());
        return new GetHomeUserRecentDetailRes(getUserRecentList);
    }

    public GetHomeUserRecentDetailRes getHomeUserRecentDetail(Long userId) throws BaseException {   //로그인을 하고 들어온 경우
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        List<Story> stories = storyRepository.findTop4ByOrderByCreatedDateDesc();
        List<GetHomeUserRecentDetailRes.UserRecentList> getUserRecentList = stories.stream()
                .map(userRecent -> new GetHomeUserRecentDetailRes.UserRecentList(userRecent.getExhibition().getExhibitionImg(),
                        userRecent.getUser().getUserImg(),
                        userRecent.getExhibition().getLocation(),
                        userRecent.getUser().getNickname(),
                        storyLikeRepository.existsByUserAndStory(user,userRecent),
                        true)) // 하트를 눌렀는지 확인
                .collect(Collectors.toList());
        return new GetHomeUserRecentDetailRes(getUserRecentList);
    }

    public GetHomeUserViewDetailRes getHomeUserViewDetailRes() {    //로그인을 안하고 들어온경우
        List<Story> userViewList = storyRepository.findTop4ByOrderByViewCountDesc();
        List<GetHomeUserViewDetailRes.UserViewList> getUserViewList = userViewList.stream()
                .map(userView -> new GetHomeUserViewDetailRes.UserViewList(userView.getExhibition().getExhibitionImg(),
                        userView.getUser().getUserImg(),
                        userView.getExhibition().getLocation(),
                        userView.getUser().getNickname(),
                        false,
                        false))
                .collect(Collectors.toList());
        return new GetHomeUserViewDetailRes(getUserViewList);
    }

    public GetHomeUserViewDetailRes getHomeUserViewDetailRes(Long userId) throws BaseException {    //로그인을 하고 들어온경우
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        List<Story> userViewList = storyRepository.findTop4ByOrderByViewCountDesc();
        List<GetHomeUserViewDetailRes.UserViewList> getUserViewList = userViewList.stream()
                .map(userView -> new GetHomeUserViewDetailRes.UserViewList(userView.getExhibition().getExhibitionImg(),
                        userView.getUser().getUserImg(),
                        userView.getExhibition().getLocation(),
                        userView.getUser().getNickname(),
                        storyLikeRepository.existsByUserAndStory(user,userView),
                        true))
                .collect(Collectors.toList());
        return new GetHomeUserViewDetailRes(getUserViewList);
    }

    public GetHomeUserLikeDetailRes getHomeUserLikeDetailRes() {    //로그인을 안하고 들어온경우
        List<Story> userLikeList = storyRepository.findTop4ByOrderByLikeCountDesc();
        List<GetHomeUserLikeDetailRes.UserLikeList> getLikeViewList = userLikeList.stream()
                .map(userLike -> new GetHomeUserLikeDetailRes.UserLikeList(userLike.getExhibition().getExhibitionImg(),
                        userLike.getUser().getUserImg(),
                        userLike.getExhibition().getLocation(),
                        userLike.getUser().getNickname(),
                        false,
                        false))
                .collect(Collectors.toList());
        return new GetHomeUserLikeDetailRes(getLikeViewList);
    }
    public GetHomeUserLikeDetailRes getHomeUserLikeDetailRes(Long userId) throws BaseException { //로그인을 하고 들어온경우
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        List<Story> userLikeList = storyRepository.findTop4ByOrderByLikeCountDesc();
        List<GetHomeUserLikeDetailRes.UserLikeList> getLikeViewList = userLikeList.stream()
                .map(userLike -> new GetHomeUserLikeDetailRes.UserLikeList(userLike.getExhibition().getExhibitionImg(),
                        userLike.getUser().getUserImg(),
                        userLike.getExhibition().getLocation(),
                        userLike.getUser().getNickname(),
                        storyLikeRepository.existsByUserAndStory(user,userLike),
                        true))
                .collect(Collectors.toList());
        return new GetHomeUserLikeDetailRes(getLikeViewList);
    }

}
