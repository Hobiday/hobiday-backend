package com.example.hobiday_backend.domain.perform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PerformAllResponse {
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

    private String facilityId;          // 시설상세ID
    private String cast;                // 공연 출연진
    private String runtime;             // 공연 런타임 (1시간 30분)
    private String perform;             // 공연 관람 연령 (만 12세 이상)
    private String ticketPrice;         // 티켓가격 (전석 30,000원)
    //    private String sty;           // 줄거리
    private String storyUrl;            // [상위 여러개에서 1개]소개 이미지 (url)
    private String showtime;            // 공연시간
    private String reservationChannel;  // [상위 여러개에서 1개]예약 방법
    private String reservationUrl;      // [상위 여러개에서 1개]예약 사이트

    private int wishCount;      // 좋아요 개수
    private int feedCount;      // 피드 개수
}
