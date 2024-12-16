package com.example.hobiday_backend.domain.wishlist.controller;

import com.example.hobiday_backend.domain.member.service.MemberService;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.exception.ProfileErrorCode;
import com.example.hobiday_backend.domain.profile.exception.ProfileException;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import com.example.hobiday_backend.domain.profile.service.ProfileService;
import com.example.hobiday_backend.domain.wishlist.dto.WishMessageResponse;
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
    private final MemberService memberService;
    private final ProfileRepository profileRepository;

    // 위시리스트 모두 조회 (위시 누른 최근)
    @GetMapping("/wishlist")
    public ApiResponse<List<WishResponse>> getWishlistAll(@RequestHeader("Authorization") String token,
                                                          @RequestParam("rowStart") String rowStart,
                                                          @RequestParam("rowEnd") String rowEnd) {
        return ApiResponse.success(wishlistService.getWishlistAll(getProfileIdByToken(token), rowStart, rowEnd));
    }
    
    // 위시리스트 장르별 조회
    @GetMapping("/wishlist/{genre}")
    public ApiResponse<List<WishResponse>> getWishlistByGenre(@RequestHeader("Authorization") String token,
                                                              @RequestParam("rowStart") String rowStart,
                                                              @RequestParam("rowEnd") String rowEnd,
                                                       @PathVariable String genre) {
        return ApiResponse.success(wishlistService.getWishlistByGenre(getProfileIdByToken(token), rowStart, rowEnd, genre));
    }

    // 위시 등록
    @PostMapping("/wishlist/{performId}")
    public ApiResponse<WishMessageResponse> registerWish(@RequestHeader("Authorization") String token,
                                                  @PathVariable("performId") String performId) {
        return ApiResponse.success(wishlistService.register(getProfileIdByToken(token), performId));
    }

    // 위시 삭제
    @DeleteMapping("/wishlist/{performId}")
    public ApiResponse<WishMessageResponse> clearWish(@RequestHeader("Authorization") String token,
                                                      @PathVariable("performId") String performId) {
        return ApiResponse.success(wishlistService.clear(getProfileIdByToken(token), performId));
    }

    // 토큰으로 프로필 리턴
    private Long getProfileIdByToken(String token) {
        Long memberId = memberService.getMemberIdByToken(token);
        return profileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND)).getId();
    }
}
