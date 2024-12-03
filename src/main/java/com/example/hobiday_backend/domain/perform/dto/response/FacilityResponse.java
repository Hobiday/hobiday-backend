package com.example.hobiday_backend.domain.perform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FacilityResponse {
    private String mt10id;          // 시설상세ID
    private String telno;           // 전화번호 (02-1234-5678)
    private String adres;           // 주소 (서울시 서초구 방배동)
    private String la;              // 위도
    private String lo;              // 경도
    private Boolean cafe;           // 카페 (Y/N)
    private Boolean parkinglot;     // 주차시설 (Y/N)
}
