package com.example.hobiday_backend.domain.wishlist.controller;

import com.example.hobiday_backend.domain.wishlist.dto.WishDeleteResponse;
import com.example.hobiday_backend.domain.wishlist.dto.WishResponse;
import com.example.hobiday_backend.domain.wishlist.service.WishlistService;
import com.example.hobiday_backend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Wishlist", description = "위시리스트 API")
public class WishlistController {
    private final WishlistService wishlistService;

    // 위시리스트 조회 (위시 누른 최근)
    @GetMapping("/wishlist")
    public ApiResponse<List<WishResponse>> getWishlist(@RequestHeader("Authorization") String token) {

    }

    //////위시 등록하거나 삭제하면 공연쪽 좋아요 개수 변해야함

    // 위시 등록
    @PostMapping("/wishlist/{performId}")
    public ApiResponse<WishResponse> registerWish(@RequestHeader("Authorization") String token
            , @PathVariable("performId") String performId) {

    }

    // 위시 삭제
    @DeleteMapping("/wishlist/{performId}")
    public ApiResponse<WishDeleteResponse> deleteWish(@RequestHeader("Authorization") String token
            , @PathVariable("performId") String performId) {

    }
}
