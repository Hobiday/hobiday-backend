package com.example.hobiday_backend.domain.users.controller;

import com.example.hobiday_backend.domain.users.dto.LogoutMessageResponse;
import com.example.hobiday_backend.domain.users.dto.UserResponse;
import com.example.hobiday_backend.domain.users.entity.User;
import com.example.hobiday_backend.domain.users.service.UserService;
import com.example.hobiday_backend.global.jwt.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Users", description = "회원관리 API")
public class UserController {
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    // 로그아웃
    @Operation(summary="로그아웃 API", description = "회원ID에 해당하는 리프레시 토큰을 DB에서 삭제하여 로그아웃 합니다." +
            "\n클라이언트 로컬 스토리지의 액세스 토큰과 쿠키에 저장된 리프레시 토큰은 프론트에서 제거해 주세요.")
    @DeleteMapping("/api/users/logout")
    public ResponseEntity<LogoutMessageResponse> deleteRefreshToken() {
        refreshTokenService.delete();
        return ResponseEntity.ok()
                .body(new LogoutMessageResponse("logout success"));
    }

    // 사용자(카카오) 정보 반환
    @Operation(summary="회원(카카오) 정보 반환 API", description = "헤더 액세스 토큰으로 요청 받아 회원(카카오) 정보를 반환합니다.")
    @GetMapping("/api/users")
    public ResponseEntity<UserResponse> findId(@RequestHeader("Authorization") String token){
        Long userId = userService.getUserIdByToken(token);
        User user = userService.findById(userId);
        return ResponseEntity.ok(UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build());
    }
}
