package com.example.hobiday_backend.domain.perform.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "Performs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Perform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prfnm; // 공연명
    private String prfpdto; // 종료일
    private String prfpdfrom; // 시작일
    private String genrenm; // 장르명
    private String fcltynm; // 공연시설명
    private String area; // 지역명
    private String poster; // 공연포스터 경로



    @Builder
    public Perform(String prfnm, String prfpdfrom, String prfpdto, String genrenm, String fcltynm, String area, String poster) {
        this.prfnm = prfnm;
        this.prfpdfrom = prfpdfrom;
        this.prfpdto = prfpdto;
        this.genrenm = genrenm;
        this.fcltynm = fcltynm;
        this.area = area;
        this.poster = poster;
    }
}

