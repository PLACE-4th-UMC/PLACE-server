package com.umc.place.home.service;

import com.umc.place.exhibition.entity.Exhibition;
import com.umc.place.exhibition.repository.ExhibitionRepository;
import com.umc.place.home.dto.GetHomeUserLikeDetailRes;
import com.umc.place.home.dto.GetHomeUserRecentDetailRes;
import com.umc.place.home.dto.GetHomeUserViewDetailRes;
import com.umc.place.story.entity.Story;
import com.umc.place.story.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final StoryRepository storyRepository;

    public GetHomeUserRecentDetailRes getHomeUserRecentDetail() {
//        List<Exhibition> userRecentList = exhibitionRepository.findTop4ByOrderByCreatedDateDesc();
//        List<GetHomeUserRecentDetailRes.UserRecentList> getuserRecentList = userRecentList.stream()
//                .map(userRecent -> new GetHomeUserRecentDetailRes.UserRecentList(userRecent.getExhibitionImg(), userRecent.getExhibitionName(), userRecent.getLocation(), userRecent.getArtist()))
//                .collect(Collectors.toList());
//        return new GetHomeUserRecentDetailRes(getuserRecentList);
        List<Story> stories = storyRepository.findTop4ByOrderByCreatedDateDesc();
        List<GetHomeUserRecentDetailRes.UserRecentList> getUserRecentList = stories.stream()
                .map(userRecent -> new GetHomeUserRecentDetailRes.UserRecentList(userRecent.getExhibition().getExhibitionImg(),
                        userRecent.getUser().getUserImg(),
                        userRecent.getExhibition().getLocation(),
                        userRecent.getUser().getNickname()))
                .collect(Collectors.toList());;
        return new GetHomeUserRecentDetailRes(getUserRecentList);
    }

    public GetHomeUserViewDetailRes getHomeUserViewDetailRes() {
//        List<Exhibition> userViewList = storyRepository.findTop4ByOrderByViewCountDesc();
//        List<GetHomeUserViewDetailRes.UserViewList> getuserViewList = userViewList.stream()
//                .map(userView -> new GetHomeUserViewDetailRes.UserViewList(userView.getExhibitionImg(), userView.getExhibitionName(), userView.getLocation(), userView.getArtist()))
//                .collect(Collectors.toList());
//        return new GetHomeUserViewDetailRes(getuserViewList);
        List<Story> userViewList = storyRepository.findTop4ByOrderByViewCountDesc();
        List<GetHomeUserViewDetailRes.UserViewList> getUserViewList = userViewList.stream()
                .map(userView -> new GetHomeUserViewDetailRes.UserViewList(userView.getExhibition().getExhibitionImg(), userView.getUser().getUserImg(), userView.getExhibition().getLocation(), userView.getUser().getNickname()))
                .collect(Collectors.toList());
        return new GetHomeUserViewDetailRes(getUserViewList);
    }

    public GetHomeUserLikeDetailRes getHomeUserLikeDetailRes() {
        List<Story> userLikeList = storyRepository.findTop4ByOrderByLikeCountDesc();
        List<GetHomeUserLikeDetailRes.UserLikeList> getLikeViewList = userLikeList.stream()
                .map(userLike -> new GetHomeUserLikeDetailRes.UserLikeList(userLike.getExhibition().getExhibitionImg(), userLike.getUser().getUserImg(), userLike.getExhibition().getLocation(), userLike.getUser().getNickname()))
                .collect(Collectors.toList());
        return new GetHomeUserLikeDetailRes(getLikeViewList);
    }
}
