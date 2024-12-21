package com.example.hobiday_backend.domain.feed.controller;

import com.example.hobiday_backend.domain.feed.dto.FeedReq;
import com.example.hobiday_backend.domain.feed.dto.FeedRes;
import com.example.hobiday_backend.domain.feed.service.FeedService;
import com.example.hobiday_backend.domain.member.service.MemberService;
import com.example.hobiday_backend.global.dto.ApiResponse;
import com.example.hobiday_backend.global.dto.file.PreSignedUrlRequest;
import com.example.hobiday_backend.global.dto.file.PresignedUrlResponse;
import com.example.hobiday_backend.global.s3.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Feed", description = "피드 API와 파일등록 API")
public class FeedController {
    private final FeedService feedService;
    private final MemberService memberService;
    private final FileService fileService;


    //파일등록 API
    @Operation(summary = "Presigned URL 요청", description = "파일 업로드를 위한 presigned URL을 생성하는 API" +
            "\n버킷명/prefix/fileName 위치에 파일 생성")
    @PostMapping("/v1/file")
    public ResponseEntity<PresignedUrlResponse> uploadPhoto(@RequestBody PreSignedUrlRequest presignedUrlRequest) {
        // Presigned URL 생성 요청 처리
        PresignedUrlResponse response = fileService.getUploadPresignedUrl(
                presignedUrlRequest.getPrefix(),
                presignedUrlRequest.getFileName()
        );

        // 성공적인 응답을 반환
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "최신순 피드 조회", description = "최신순으로 모든 피드를 조회하는 API이며 요청 값으로 토큰만 받습니다" +
            "현재 시간을 기준으로 몇분전 게시물인지도 반환")
    @GetMapping("/feeds")
    public ApiResponse<List<FeedRes>> getFeedsByLatest(@RequestHeader("Authorization") String token) {
        Long userId = memberService.getMemberIdByToken(token);
        List<FeedRes> feedsRes = feedService.getFeedsByLatest(userId);
        return ApiResponse.success(feedsRes);
    }

    @Operation(summary = "좋아요 순 피드 조회", description = "좋아요 순으로 모든 피드를 조회하는 API이며 요청 값으로 토큰만 받습니다" +
            "현재 시간을 기준으로 몇분전 게시물인지도 반환")
    @GetMapping("/feeds/likes")
    public ApiResponse<List<FeedRes>> getFeedsByLikes(@RequestHeader("Authorization") String token) {
        Long userId = memberService.getMemberIdByToken(token);
        List<FeedRes> feedsRes = feedService.getFeedsByLikes(userId);
        return ApiResponse.success(feedsRes);
    }

    //피드 작성
    @Operation(summary = "피드 작성", description = "요청헤더에는 토큰 // 요청바디에는 내용,주제,파일url,해시태그를 받습니다")
    @PostMapping("/feeds")
    public ApiResponse<FeedRes> createFeed(@RequestBody FeedReq feedReq,
                                           @RequestHeader("Authorization") String token) {
        // 1. 토큰을 사용해 사용자 ID 가져오기
        Long userId = memberService.getMemberIdByToken(token);
        // 2. 피드 생성
        FeedRes feedRes = feedService.createFeed(feedReq, userId);
        return ApiResponse.success(feedRes);
    }

    //피드 수정
    @Operation(summary = "피드 수정", description = "토큰과 feedId를 받는 피드 수정 api")
    @PatchMapping("/feeds/{feedId}")
    public ApiResponse<FeedRes> updateFeed(@RequestHeader("Authorization") String token,
                                           @RequestBody FeedReq feedReq,
                                           @PathVariable Long feedId) {
        Long memberId = memberService.getMemberIdByToken(token);
        FeedRes feedRes = feedService.updateFeed(feedReq, memberId, feedId);
        return ApiResponse.success(feedRes);
    }

    //피드 삭제
    @Operation(summary = "피드 삭제", description = "토큰과 feedId를 받는 피드 삭제 api")
    @DeleteMapping("/feeds/{feedId}")
    public ApiResponse<Void> deleteFeed(@RequestHeader("Authorization") String token,
                                        @PathVariable Long feedId) {
        Long memberId = memberService.getMemberIdByToken(token);
        feedService.deleteFeed(memberId, feedId);
        return ApiResponse.success(null);
    }

    // 프로필 단일 피드 조회
    @Operation(summary = "프로필에서 단일 피드 조회", description = "피드의 id를 path명으로 받습니다" +
            "프로필에서 단일 피드 조회를 하는 API")
    @GetMapping("/profiles/feeds/{feedId}")
    public ApiResponse<FeedRes> getFeedById(@PathVariable Long feedId) {
        FeedRes feedRes = feedService.getFeedById(feedId);
        return ApiResponse.success(feedRes);
    }

    // 프로필 하위 전체 피드 조회
    @Operation(summary = "프로필 하위의 전체 피드 조회", description = "프로필 하위에 전체 피드를 조회하는 API")
    @GetMapping("/profiles/{profileId}/feeds")
    public ApiResponse<List<FeedRes>> getProfileFeeds(@PathVariable Long profileId) {
        List<FeedRes> profileFeeds = feedService.getProfileFeeds(profileId);
        return ApiResponse.success(profileFeeds);
    }

}
