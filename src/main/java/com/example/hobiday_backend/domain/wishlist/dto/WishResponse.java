package com.example.hobiday_backend.domain.wishlist.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WishResponse {
    private Long wishId;
    private Long profileId;
    private String mt20id;
}
