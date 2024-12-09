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
    private List<String> profileGenres;
    private String profileIntroduction;
    private String profileImageUrl;

}