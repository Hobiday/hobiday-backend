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
public class PerformRecommendListResponse {
    private String performId;          // 공연상세ID
    private String performName;           // 공연명
    private String genreName;

    // 추천 검색어 목록 반환용
    public PerformRecommendListResponse(Perform perform){
        this.performId = perform.getMt20id();
        this.performName = perform.getPrfnm();
        this.genreName = perform.getGenrenm();
    }
}
