package com.example.hobiday_backend.domain.follow.controller;

import com.example.hobiday_backend.domain.follow.dto.request.FollowRequest;
import com.example.hobiday_backend.domain.follow.dto.response.FollowMessageResponse;
import com.example.hobiday_backend.domain.follow.dto.response.FollowResponse;
import com.example.hobiday_backend.domain.follow.service.FollowService;
import com.example.hobiday_backend.domain.member.service.MemberService;
import com.example.hobiday_backend.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController {
    private final FollowService followService;
    private final MemberService memberService;

    @PostMapping("/profiles/follow")
    public ApiResponse<FollowMessageResponse> toggleFollow(@RequestHeader("Authorization") String token,
                                                           @RequestParam("targetProfileId") Long targetProfileId) {
        return ApiResponse.success(followService.toggleFollow(token, targetProfileId));
    }

    // 팔로잉 목록 조회
    @GetMapping("/profiles/{profileId}/following")
    public ApiResponse<List<FollowResponse>> getFollowingList(@PathVariable Long profileId) {
        List<FollowResponse> followingList = followService.getFollowingList(profileId);
        return ApiResponse.success(followingList);
    }

    // 팔로워 목록 조회
    @GetMapping("/profiles/{profileId}/followers")
    public ApiResponse<List<FollowResponse>> getFollowerList(@PathVariable Long profileId) {
        List<FollowResponse> followerList = followService.getFollowerList(profileId);
        return ApiResponse.success(followerList);
    }

}
