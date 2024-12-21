package com.example.hobiday_backend.domain.follow.controller;

import com.example.hobiday_backend.domain.follow.dto.response.FollowMessageResponse;
import com.example.hobiday_backend.domain.follow.dto.response.FollowResponse;
import com.example.hobiday_backend.domain.follow.service.FollowService;
import com.example.hobiday_backend.domain.member.service.MemberService;
import com.example.hobiday_backend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Follow", description = "팔로우 관련 API") // 전체 컨트롤러 설명
public class FollowController {
    private final FollowService followService;
    private final MemberService memberService;

    @PostMapping("/profiles/follow")
    @Operation(
            summary = "팔로우/언팔로우 토글",
            description = "사용자가 다른 사용자를 팔로우하거나 이미 팔로우한 상태라면 언팔로우합니다."
    )
    public ApiResponse<FollowMessageResponse> toggleFollow(
            @RequestHeader("Authorization") String token,
            @RequestParam("targetProfileId") Long targetProfileId) {
        return ApiResponse.success(followService.toggleFollow(token, targetProfileId));
    }

    // 팔로잉 목록 조회
    @GetMapping("/profiles/{profileId}/following")
    @Operation(
            summary = "팔로잉 목록 조회",
            description = "특정 사용자가 팔로우하고 있는 사용자 목록을 조회합니다."
    )
    public ApiResponse<List<FollowResponse>> getFollowingList(@PathVariable Long profileId) {
        List<FollowResponse> followingList = followService.getFollowingList(profileId);
        return ApiResponse.success(followingList);
    }

    // 팔로워 목록 조회
    @GetMapping("/profiles/{profileId}/followers")
    @Operation(
            summary = "팔로워 목록 조회",
            description = "특정 사용자를 팔로우하고 있는 사용자 목록을 조회합니다."
    )
    public ApiResponse<List<FollowResponse>> getFollowerList(@PathVariable Long profileId) {
        List<FollowResponse> followerList = followService.getFollowerList(profileId);
        return ApiResponse.success(followerList);
    }
}
