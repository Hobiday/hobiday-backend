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

/*    // 추천순 피드 조회
    @Operation(summary = "추천순 피드 조회 기능", description = "피드를 추천순으로 조회합니다.")
    @GetMapping("/recommendation")
    public ResponseEntity<List<FeedRes>> getFeedsByLikeCount() {
        List<FeedRes> feedResList = feedService.getFeedsByLikeCount();
        return ResponseEntity.ok(feedResList);
    }

    // 최신순 피드 조회
    @Operation(summary = "최신순 피드 조회 기능", description = "피드를 최신순으로 조회합니다.")
    @GetMapping("/latest")
    public ResponseEntity<List<FeedRes>> getFeedsByLatest() {
        List<FeedRes> feedResList = feedService.getFeedsByLatest();
        return ResponseEntity.ok(feedResList);
    }*/

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
}
