package com.example.hobiday_backend.domain.like.controller;

import com.example.hobiday_backend.domain.like.dto.LikeRes;
import com.example.hobiday_backend.domain.like.service.LikeService;
import com.example.hobiday_backend.domain.member.service.MemberService;
import com.example.hobiday_backend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Tag(name = "Like", description = "좋아요 API") // 컨트롤러 태그
public class LikeController {

    private final LikeService likeService;
    private final MemberService memberService;

    @Operation(
            summary = "좋아요 추가/취소",
            description = "좋아요 상태를 토글한 뒤, 좋아요 상태와 갯수를 반환하는 API",
            parameters = {
                    @Parameter(name = "feedId", description = "좋아요를 추가/취소할 피드의 ID", required = true),
                    @Parameter(name = "token", description = "사용자 인증 토큰 (헤더)", required = true)
            }
    )
    @PostMapping("/{feedId}")
    public ApiResponse<LikeRes> toggleLike(
            @PathVariable Long feedId,
            @RequestHeader("Authorization") String token) {
        Long userId = memberService.getMemberIdByToken(token);
        LikeRes likeRes = likeService.toggleLike(feedId, userId);
        return ApiResponse.success(likeRes);
    }
}

