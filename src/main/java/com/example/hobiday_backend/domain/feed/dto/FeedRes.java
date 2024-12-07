package com.example.hobiday_backend.domain.feed.dto;

import com.example.hobiday_backend.domain.feed.entity.HashTag;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class FeedRes {
    private String contents;
    private String profileName;
    private Long profileId;
    // 프로필 사진도 추가로 들어가야 함
    private List<String> hashTag;
    private List<String> feedFiles;
    private int likeCount;
    private boolean isLiked;
    // 공연 정보
    private String performName; // 공연 이름
}
