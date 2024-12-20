package com.example.hobiday_backend.domain.wishlist.controller;

import com.example.hobiday_backend.domain.member.service.MemberService;
import com.example.hobiday_backend.domain.profile.exception.ProfileErrorCode;
import com.example.hobiday_backend.domain.profile.exception.ProfileException;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import com.example.hobiday_backend.domain.wishlist.dto.WishMessageResponse;
import com.example.hobiday_backend.domain.wishlist.dto.WishResponse;
import com.example.hobiday_backend.domain.wishlist.service.WishlistService;
import com.example.hobiday_backend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary="전체 위시 조회", description="위시 최근에 등록된 순서대로 전체 조회 | rowStart = DB시작값(0부터 시작), rowEnd = DB끝값")
    @GetMapping("/wishlist")
    public ApiResponse<List<WishResponse>> getWishlistAll(@RequestHeader("Authorization") String token,
                                                          @RequestParam("rowStart") String rowStart,
                                                          @RequestParam("rowEnd") String rowEnd) {
        return ApiResponse.success(wishlistService.getWishlistAll(getProfileIdByToken(token), rowStart, rowEnd));
    }

    // 위시리스트 장르별 조회
    @Operation(summary="장르별 조회", description="장르별 조회는 몇개 안되니 전부 응답")
    @GetMapping("/wishlist/genre")
    public ApiResponse<List<WishResponse>> getWishlistByGenre(@RequestHeader("Authorization") String token,
                                                              @RequestParam("category") String genre) {
        return ApiResponse.success(wishlistService.getWishlistByGenre(getProfileIdByToken(token), genre));
    }

    // 위시 등록
    @Operation(summary="위시 등록", description="사용자와 공연ID(PF255667) 매칭시켜 위시 등록 | 중복 등록시 예외 적용함 | 공연상세 페이지에서 좋아요 클릭시")
    @PostMapping("/wishlist/{performId}")
    public ApiResponse<WishMessageResponse> registerWish(@RequestHeader("Authorization") String token,
                                                  @PathVariable("performId") String performId) {
        return ApiResponse.success(wishlistService.register(getProfileIdByToken(token), performId));
    }

    // 위시 삭제
    @DeleteMapping("/wishlist/{performId}")
    @Operation(summary="위시 해제", description="사용자와 공연ID(PF255667) 매칭시켜 위시 해제 | 중복 삭제시 예외 적용함 | 공연상세 페이지 좋아요 또는 위시리스트에서 해제")
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
