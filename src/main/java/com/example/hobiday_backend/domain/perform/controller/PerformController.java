package com.example.hobiday_backend.domain.perform.controller;

import com.example.hobiday_backend.domain.feed.dto.FeedRes;
import com.example.hobiday_backend.domain.member.service.MemberService;
import com.example.hobiday_backend.domain.perform.dto.response.*;
import com.example.hobiday_backend.domain.perform.service.PerformService;
import com.example.hobiday_backend.domain.profile.exception.ProfileErrorCode;
import com.example.hobiday_backend.domain.profile.exception.ProfileException;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import com.example.hobiday_backend.domain.profile.service.ProfileService;
import com.example.hobiday_backend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name="Performs", description="현재의 응답하는 공연개수, 기준은 임시이고 달라질 수 있음")
public class PerformController {
    private final PerformService performService;
    private final MemberService memberService;
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;


    // 전체 공연 조회
    @Operation(summary="전체 공연 조회(공연시작일 빠를수록)", description="전체에서 공연중을 우선 조회 |rowStart = DB시작값(0부터 시작), rowEnd = DB끝값\"")
    @GetMapping("/performs")
    public ApiResponse<List<PerformResponse>> getPerformsAll(@RequestHeader("Authorization") String token,
                                                             @RequestParam String rowStart,
                                                             @RequestParam String rowEnd){
//        Long memberId = memberService.getMemberIdByToken(token);
//        List<String> profileGenreList = profileService.getProfileByMemberId(memberId).getProfileGenres();
        return ApiResponse.success(performService.getPerformsAll(rowStart, rowEnd));
    }

    // 전체 공연 조회2
    @Operation(summary="전체 공연 조회(공연종료 임박할수록)", description="rowStart = DB시작값(0부터 시작), rowEnd = DB끝값\"")
    @GetMapping("/performs/deadline")
    public ApiResponse<List<PerformResponse>> getPerformsAllDeadline(@RequestHeader("Authorization") String token,
                                                             @RequestParam String rowStart,
                                                             @RequestParam String rowEnd){
//        Long memberId = memberService.getMemberIdByToken(token);
//        List<String> profileGenreList = profileService.getProfileByMemberId(memberId).getProfileGenres();
        return ApiResponse.success(performService.getPerformsAllDeadline(rowStart, rowEnd));
    }

    // 장르별 공연 조회
    @Operation(summary="장르별 공연 목록 조회", description="바디에 장르명 입력 | 공연중을 우선 조회 |rowStart = DB시작값(0부터 시작), rowEnd = DB끝값")
    @GetMapping("/performs/genre")
    public ApiResponse<List<PerformResponse>> getPerformsByGenre(@RequestParam String rowStart,
                                                                 @RequestParam String rowEnd,
                                                                 @RequestParam String genre) {
        return ApiResponse.success(performService.getPerformListByGenre(genre, rowStart, rowEnd));
    }

    // 공연 추천 검색어 목록
    @GetMapping("/performs/search/recommends")
    @Operation(summary="공연 추천 검색어 목록", description="프로필 선택한 장르로 10개")
    public ApiResponse<List<PerformRecommendListResponse>> getPerformsByRecommends(@RequestHeader("Authorization") String token) {
        Long memberId = memberService.getMemberIdByToken(token);
        List<String> profileGenreList = profileService.getProfileByMemberId(memberId).getProfileGenres();
        return ApiResponse.success(performService.getPerformsByRecommends(profileGenreList));
    }

    // 공연 상세 검색 조회
    @Operation(summary="공연 상세 검색",description="쿼리파라미터에 검색어, 장르들, 지역들 | 설명은 서버알림방" )
    @GetMapping("/performs/search/detail")
    public ApiResponse<List<PerformResponse>> getPerformsBySearchDetails(@RequestParam String keyword,
                                                                                      @RequestParam List<String> genres,
                                                                                      @RequestParam List<String> areas){

        return ApiResponse.success(performService.getPerformsBySearchDetails(keyword, genres, areas));
    }

    // 공연명으로 검색 결과 조회
    @Operation(summary="공연명으로 검색 결과 조회", description="입력한 이름을 포함하는 공연 조회 ex) 주말, 마술")
    @GetMapping("/performs/search")
    public ApiResponse<List<PerformResponse>> getPerformsBySearch(@RequestParam String keyword) {
//        log.info("키워드: " + performSearchRequest.keyword);
        return ApiResponse.success(performService.getPerformsBySearch(keyword));
    }

    // 공연 기본정보 조회
    @Operation(summary="공연 기본정보 조회", description="Kopis의 공연상세ID로 조회")
    @GetMapping("/performs/{performId}")
    public ApiResponse<PerformResponse> getPerform(@PathVariable String performId){
        return ApiResponse.success(performService.getPerform(performId));
    }

    // 공연 상세정보 조회
    @Operation(summary="공연 상세정보 조회", description="Kopis의 공연상세ID로 조회")
    @GetMapping("/performs/perform/{performId}")
    public ApiResponse<PerformDetailResponse> getPerformDetail(@RequestHeader("Authorization") String token,
                                                               @PathVariable("performId") String performId){;
        Long profileId = profileRepository.findByMemberId(memberService.getMemberIdByToken(token))
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND)).getId();
        return ApiResponse.success(performService.getPerformDetailResponse(performId, profileId));
    }

    // 시설 정보 조회
    @Operation(summary="시설 정보 조회", description="Kopis의 시설상세ID로 조회")
    @GetMapping("/performs/facility/{facilityId}")
    public ApiResponse<FacilityResponse> getFacility(@PathVariable("facilityId") String placeId){
        return ApiResponse.success(performService.getFacilityResponse(placeId));
    }

    // 공연 기본&상세정보 조회
    @Operation(summary="공연 기본&상세정보 조회", description="Kopis의 공연상세ID로 조회")
    @GetMapping("/performs/all/{performId}")
    public ApiResponse<PerformAllResponse> getPerformAll(@RequestHeader("Authorization") String token,
                                                         @PathVariable String performId){
        Long profileId = profileRepository.findByMemberId(memberService.getMemberIdByToken(token))
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND)).getId();
        return ApiResponse.success(performService.getPerformAll(performId, profileId));
    }

    // 공연으로 작성한 피드 전체 조회
    @Operation(summary="공연으로 작성한 피드 전체 조회", description="공연ID PF12345 보내서 피드 전체 조회")
    @GetMapping("/performs/feeds/{performId}")
    public ApiResponse<List<FeedRes>> getFeedsByPerformId(@PathVariable String performId){
        return ApiResponse.success(performService.getFeedsByPerformId(performId));
    }
}