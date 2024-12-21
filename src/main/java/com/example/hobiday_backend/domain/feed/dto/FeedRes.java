package com.example.hobiday_backend.domain.feed.dto;

import com.example.hobiday_backend.domain.feed.entity.Feed;
import com.example.hobiday_backend.domain.feed.entity.FeedFile;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class FeedRes {
    private Long feedId;
    private String contents;
    private String profileName;
    private Long profileId;
    private String profileImageUrl;
    private List<String> hashTag;
    private List<String> feedFiles;
    private Integer likeCount;
    private boolean isLiked;
    private Integer commentCount;

    // 공연 정보
    private String performId;          // 공연상세ID
    private String performName;           // 공연명
    private String startDate;       // 시작일 (2016.05.12)
    private String endDate;         // 종료일
    private String genreName;         // 장르명
    private String performState;        // 공연상태 (예정, 중, 완료)
    private String placeName;         // 공연시설명 (피가로아트홀)
    private Boolean openRun;        // 오픈런 (Y/N)
    private String area;            // 지역명 (서울)
    private String poster;          // 공연포스터 경로 (url)
    private Integer performLikeCount;      // 좋아요 개수

    // 상대 시간
    private String relativeTime;
}
