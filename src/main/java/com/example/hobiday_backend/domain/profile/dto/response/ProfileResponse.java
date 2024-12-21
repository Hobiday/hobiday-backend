package com.example.hobiday_backend.domain.profile.dto.response;

import com.example.hobiday_backend.domain.profile.entity.Profile;
import lombok.*;

import java.util.List;

import static com.example.hobiday_backend.domain.perform.util.GenreCasting.getGenreToList;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProfileResponse {
    private Long profileId;
    private Long memberId;
    private String profileNickname;
    private String profileEmail;
    private List<String> profileGenres;
    private String profileIntroduction;
    private String profileImageUrl;
    private int totalFeedCount;
    private int followerCount;
    private int followingCount;

    public static ProfileResponse from(Profile profile){
        return ProfileResponse.builder()
                .profileId(profile.getId())
                .memberId(profile.getMember().getId()) // 방2
                .profileNickname(profile.getProfileNickname())
                .profileEmail(profile.getProfileEmail())
                .profileGenres(getGenreToList(profile.getProfileGenre()))
                .profileIntroduction(profile.getProfileIntroduction())
                .profileImageUrl(profile.getProfileImageUrl())
                .build();
    }

    public static ProfileResponse of(Profile profile, int totalFeedCount, int followerCount, int followingCount) {
        return ProfileResponse.builder()
                .profileId(profile.getId())
                .memberId(profile.getMember().getId())
                .profileNickname(profile.getProfileNickname())
                .profileEmail(profile.getProfileEmail())
                .profileGenres(getGenreToList(profile.getProfileGenre()))
                .profileIntroduction(profile.getProfileIntroduction())
                .profileImageUrl(profile.getProfileImageUrl())
                .totalFeedCount(totalFeedCount)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }
}