package com.umc.place.home.service;

import com.umc.place.exhibition.entity.Exhibition;
import com.umc.place.exhibition.repository.ExhibitionRepository;
import com.umc.place.home.dto.GetHomeUserLikeDetailRes;
import com.umc.place.home.dto.GetHomeUserRecentDetailRes;
import com.umc.place.home.dto.GetHomeUserViewDetailRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final ExhibitionRepository exhibitionRepository;

    public GetHomeUserRecentDetailRes getHomeUserRecentDetail() {
        List<Exhibition> userRecentList = exhibitionRepository.findTop4ByOrderByCreatedDateDesc();
        List<GetHomeUserRecentDetailRes.UserRecentList> getuserRecentList = userRecentList.stream()
                .map(userRecent -> new GetHomeUserRecentDetailRes.UserRecentList(userRecent.getExhibitionImg(), userRecent.getExhibitionName(), userRecent.getLocation(), userRecent.getArtist()))
                .collect(Collectors.toList());
        return new GetHomeUserRecentDetailRes(getuserRecentList);
    }

    public GetHomeUserViewDetailRes getHomeUserViewDetailRes() {
        List<Exhibition> userViewList = exhibitionRepository.findTop4ByOrderByViewCountDesc();
        List<GetHomeUserViewDetailRes.UserViewList> getuserViewList = userViewList.stream()
                .map(userView -> new GetHomeUserViewDetailRes.UserViewList(userView.getExhibitionImg(), userView.getExhibitionName(), userView.getLocation(), userView.getArtist()))
                .collect(Collectors.toList());
        return new GetHomeUserViewDetailRes(getuserViewList);
    }

    public GetHomeUserLikeDetailRes getHomeUserLikeDetailRes() {
        List<Exhibition> userLikeList = exhibitionRepository.findTop4ByOrderByLikeCountDesc();
        List<GetHomeUserLikeDetailRes.UserLikeList> getuserLikeList = userLikeList.stream()
                .map(userLike -> new GetHomeUserLikeDetailRes.UserLikeList(userLike.getExhibitionImg(), userLike.getExhibitionName(), userLike.getLocation(), userLike.getArtist()))
                .collect(Collectors.toList());
        return new GetHomeUserLikeDetailRes(getuserLikeList);
    }
}
