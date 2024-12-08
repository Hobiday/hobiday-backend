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
    private List<String> profileGenre;
    private String profileIntroduction;
    private String profileImageUrl;

    public static ProfileResponse from(Profile profile) {
        return ProfileResponse.builder()
                .profileId(profile.getId())
                .profileNickname(profile.getProfileNickname())
                .profileImageUrl(profile.getProfileImageUrl())
                .profileIntroduction(profile.getProfileIntroduction())
                .profileGenre(getGenreToList(profile.getProfileGenre()))
                .profileEmail(profile.getProfileEmail())
                .build();
    }
}