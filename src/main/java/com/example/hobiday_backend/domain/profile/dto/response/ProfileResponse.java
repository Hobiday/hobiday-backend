package com.example.hobiday_backend.domain.profile.dto.response;

import com.example.hobiday_backend.domain.profile.entity.Profile;
import lombok.*;

import java.util.List;

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

    public static ProfileResponse res(Profile profile) {
        List<String> genreList = profile.getProfileGenre() != null ?
                List.of(profile.getProfileGenre().split(",")) : List.of();

        return ProfileResponse.builder()
                .profileId(profile.getId())
                .profileNickname(profile.getProfileNickname())
                .profileEmail(profile.getProfileEmail())
                .profileGenre(genreList)
                .profileIntroduction(profile.getProfileIntroduction())
                .profileImageUrl(profile.getProfileImageUrl())
                .build();
    }

}