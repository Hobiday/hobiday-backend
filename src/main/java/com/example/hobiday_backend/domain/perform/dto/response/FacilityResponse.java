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
    private String facilityId;      // 시설상세ID
    private String facilityName;    // 시설이름
    private String telephone;       // 전화번호 (02-1234-5678)
    private String address;         // 주소 (서울시 서초구 방배동)
    private String latitude;        // 위도
    private String longitude;       // 경도
    private Boolean cafe;           // 카페 (Y/N)
    private Boolean parkingLot;     // 주차시설 (Y/N)
}
