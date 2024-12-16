package com.example.hobiday_backend.domain.wishlist.dto;

import com.example.hobiday_backend.domain.wishlist.entity.Wishlist;
import lombok.Builder;
import lombok.Getter;

@Getter
public class WishResponse {
    private Long wishId;
    private Long profileId;
    private String mt20id;
    private String genre;

    public WishResponse(Wishlist wishlist){
        this.wishId = wishlist.getId();
        this.profileId = wishlist.getProfileId();
        this.mt20id = wishlist.getMt20id();
        this.genre = wishlist.getGenre();
    }
}
