package com.example.hobiday_backend.domain.wishlist.dto;

import com.example.hobiday_backend.domain.wishlist.entity.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WishResponse {
    private Long wishId;
    private Long profileId;
    private String performId;          // 공연상세ID
    private String genreName;         // 장르명
    private String performName;           // 공연명
    private String performState;        // 공연상태 (예정, 중, 완료)
    private String placeName;         // 공연시설명 (피가로아트홀)
    private String area;            // 지역명 (서울)
    private String poster;          // 공연포스터 경로 (url)

    public WishResponse(Wishlist wishlist){
        this.wishId = wishlist.getId();
        this.profileId = wishlist.getProfileId();
        this.performId = wishlist.getMt20id();
        this.genreName = wishlist.getGenre();
        this.performName = wishlist.getPerformName();
        this.performState = wishlist.getPerformState();
        this.placeName = wishlist.getPlaceName();
        this.area = wishlist.getArea();
        this.poster = wishlist.getPoster();
    }

}
