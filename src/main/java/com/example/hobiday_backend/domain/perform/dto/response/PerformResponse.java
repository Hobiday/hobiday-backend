package com.example.hobiday_backend.domain.perform.dto.response;

import com.example.hobiday_backend.domain.perform.entity.Perform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PerformResponse {
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

    private int wishCount;      // 좋아요 개수

    // 공연 리스트 기본 반환
    public PerformResponse(Perform perform) {
        this.performId = perform.getMt20id();
        this.performName = perform.getPrfnm();
        this.startDate = perform.getPrfpdfrom();
        this.endDate = perform.getPrfpdto();
        this.genreName = perform.getGenrenm();
        this.performState = perform.getPrfstate();
        this.placeName = perform.getFcltynm();
        this.openRun = perform.getOpenrun();
        this.area = perform.getArea();
        this.poster = perform.getPoster();
        this.wishCount = perform.getWishCount();
    }

}
