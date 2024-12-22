package com.example.hobiday_backend.domain.follow.dto.response;

import com.example.hobiday_backend.domain.profile.entity.Profile;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FollowResponse {
    private Long profileId;
    private String profileNickName;
    private String profileImageUrl;
    private String profileIntroduction;
    private boolean isFollowing;

    // 생성자 추가
    public FollowResponse(Profile profile, boolean isFollowing) {
        this.profileId = profile.getId();
        this.profileNickName = profile.getProfileNickname();
        this.profileImageUrl = profile.getProfileImageUrl();
        this.profileIntroduction = profile.getProfileIntroduction();
        this.isFollowing = isFollowing;
    }
}
