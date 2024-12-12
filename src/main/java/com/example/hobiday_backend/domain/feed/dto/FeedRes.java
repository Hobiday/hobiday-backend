package com.example.hobiday_backend.domain.feed.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class FeedRes {
    private String contents;
    private String profileName;
    private Long profileId;
    private List<String> hashTag;
    private List<String> feedFiles;
    private Integer likeCount;
    private boolean isLiked; //좋아요 여부

    // 댓글 갯수
    private Integer commentCount;
    // 프로필 사진 url들
    private String profileImageUrl;

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
}
