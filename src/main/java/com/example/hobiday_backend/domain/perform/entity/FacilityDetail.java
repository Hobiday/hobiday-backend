package com.example.hobiday_backend.domain.perform.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "facility_details")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class FacilityDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ========================= 시설상세 =========================*/
    @Column(unique = true)
    private String mt10id;          // 시설상세ID
    private String telno;           // 전화번호 (02-1234-5678)
    private String adres;           // 주소 (서울시 서초구 방배동)
    private String la;              // 위도
    private String lo;              // 경도
    private Boolean cafe;           // 카페 (Y/N)
    private Boolean parkinglot;     // 주차시설 (Y/N)

    @Builder
    public FacilityDetail(String mt10id, String telno, String adres, String la, String lo, Boolean cafe, Boolean parkinglot) {
        this.mt10id = mt10id;
        this.telno = telno;
        this.adres = adres;
        this.la = la;
        this.lo = lo;
        this.cafe = cafe;
        this.parkinglot = parkinglot;
    }
}
