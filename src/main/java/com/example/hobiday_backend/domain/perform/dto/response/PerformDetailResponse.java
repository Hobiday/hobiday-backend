package com.example.hobiday_backend.domain.perform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PerformDetailResponse {
    private String performId;           // 공연상세ID
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
