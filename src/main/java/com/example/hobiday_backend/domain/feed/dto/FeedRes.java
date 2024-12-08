package com.example.hobiday_backend.domain.feed.dto;

import com.example.hobiday_backend.domain.feed.entity.HashTag;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class FeedRes {
    private Long id;
    private String contents;
    private String profileName;
    private String profileImageUrl;
    private List<HashTag> hashTag;
    private int likeCount;
    private int commentCount;
    private boolean isLiked;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}
