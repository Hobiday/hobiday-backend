package com.example.hobiday_backend.domain.perform.controller;

import com.example.hobiday_backend.domain.perform.dto.reqeust.PerformRequest;
import com.example.hobiday_backend.domain.perform.dto.response.FacilityResponse;
import com.example.hobiday_backend.domain.perform.dto.response.PerformDetailResponse;
import com.example.hobiday_backend.domain.perform.dto.response.PerformResponse;
import com.example.hobiday_backend.domain.perform.service.PerformService;
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
@Tag(name="공연 정보", description="현재의 응답하는 공연개수, 기준은 임시이고 달라질 수 있음")
public class PerformController {
    private final PerformService performService;

    // 홈화면 추천 공연 6개 | 현재는 랜덤
    @Operation(summary="홈화면 추천 공연", description="일단은 랜덤으로 가져옴")
    @GetMapping("/performs/main")
    public ApiResponse<List<PerformResponse>> getMainPerforms(/*@RequestHeader("Authorization") String token*/) {
//        Long memberId = memberService.getMemberIdByToken(token);
//        List<String> profileGenreList = profileService.getProfileByMemberId(memberId).getProfileGenre();
//        return ApiResponse.success(performService.getMainPerforms(profileGenreList));
        return ApiResponse.success(performService.getMainPerforms());
    }

    // 전체 공연 조회
    // 모든장르 x 5개 | 현재는 랜덤
    @Operation(summary="전체 공연 조회", description="모든장르 x 5개 랜덤")
    @GetMapping("/performs")
    public ApiResponse<List<PerformResponse>> getPerformsAll(){
        return ApiResponse.success(performService.getPerformsAll());
    }

    // 장르별 공연 목록 조회
    @Operation(summary="장르별 공연 목록 조회", description="바디에 장르명 정확히 입력")
    @GetMapping("/performs/genre")
    public ApiResponse<List<PerformResponse>> getPerformsByGenre(@RequestBody PerformRequest performRequest) {
        log.info("장르: " + performRequest.keyword);
        return ApiResponse.success(performService.getPerformListByGenre(performRequest.keyword));
    }

    // 공연 추천 검색어 목록
//    @GetMapping("/api/performs/search/recommends")
//    public ApiResponse<List<PerformResponse>> getPerformsByRecommends(@RequestHeader("Authorization") String token) {
//        Long memberId = memberService.getMemberIdByToken(token);
//        List<String> profileGenreList = profileService.getProfileByMemberId(memberId).getProfileGenre();
//        return ApiResponse.success(performService.getPerformsByRecommends(profileGenreList));
//    }

    // 공연명으로 검색 결과 조회
    @Operation(summary="공연명으로 검색 결과 조회", description="입력한 이름을 포함하는 공연 조회")
    @GetMapping("/performs/search")
    public ApiResponse<List<PerformResponse>> getPerformsBySearch(@RequestBody PerformRequest performRequest) {
        log.info("키워드: " + performRequest.keyword);
        return ApiResponse.success(performService.getPerformsBySearch(performRequest.keyword));
    }
    // 공연 기본정보 조회
    @Operation(summary="공연 기본정보 조회", description="Kopis의 공연상세ID로 조회")
    @GetMapping("/performs/{mt20id}")
    public ApiResponse<PerformResponse> getPerform(@PathVariable String mt20id){
        return ApiResponse.success(performService.getPerform(mt20id));
    }

    // 공연 상세정보 조회
    @Operation(summary="공연 상세정보 조회", description="Kopis의 공연상세ID로 조회")
    @GetMapping("/performs/detail/{mt20id}")
    public ApiResponse<PerformDetailResponse> getPerformDetail(@PathVariable String mt20id){
        return ApiResponse.success(performService.getPerformDetailResponse(mt20id));
    }

    // 시설 정보 조회
    @Operation(summary="시설 정보 조회", description="Kopis의 시설상세ID로 조회")
    @GetMapping("/performs/detail/{mt10id}")
    public ApiResponse<FacilityResponse> getFacility(@PathVariable("mt10id") String mt10id){
        return ApiResponse.success(performService.getFacilityResponse(mt10id));
    }
}

