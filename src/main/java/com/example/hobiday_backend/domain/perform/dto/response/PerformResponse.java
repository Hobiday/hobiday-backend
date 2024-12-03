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
    private String mt20id;          // 공연상세ID
    private String prfnm;           // 공연명
    private String prfpdfrom;       // 시작일 (2016.05.12)
    private String prfpdto;         // 종료일
    private String genrenm;         // 장르명
    private String prfstate;        // 공연상태 (예정, 중, 완료)
    private String fcltynm;         // 공연시설명 (피가로아트홀)
    private Boolean openrun;        // 오픈런 (Y/N)
    private String area;            // 지역명 (서울)
    private String poster;          // 공연포스터 경로 (url)
    private Integer likeCount;      // 좋아요 개수

    public PerformResponse(Perform perform) {
        this.mt20id = perform.getMt20id();
        this.prfnm = perform.getPrfnm();
        this.prfpdfrom = perform.getPrfpdfrom();
        this.prfpdto = perform.getPrfpdto();
        this.genrenm = perform.getGenrenm();
        this.prfstate = perform.getPrfstate();
        this.fcltynm = perform.getFcltynm();
        this.openrun = perform.getOpenrun();
        this.area = perform.getArea();
        this.poster = perform.getPoster();
        this.likeCount = perform.getLikeCount();
    }

}
