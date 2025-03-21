package com.example.hobiday_backend.domain.member.controller;

import com.example.hobiday_backend.domain.member.dto.FreePassResponse;
import com.example.hobiday_backend.domain.member.dto.MemberMessageResponse;
import com.example.hobiday_backend.domain.member.dto.MemberResponse;
import com.example.hobiday_backend.domain.member.dto.MemberSignOutResponse;
import com.example.hobiday_backend.domain.member.service.MemberService;
import com.example.hobiday_backend.global.dto.ApiResponse;
import com.example.hobiday_backend.global.jwt.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Members", description = "회원관리 API")
public class MemberController {
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;

    // 로그아웃
    @Operation(summary="로그아웃 API", description = "회원ID에 해당하는 리프레시 토큰을 DB에서 삭제하여 로그아웃 합니다." +
            "\n클라이언트 로컬 스토리지의 액세스 토큰과 쿠키에 저장된 리프레시 토큰은 프론트에서 제거해 주세요.")
    @DeleteMapping("/api/members/logout")
    public ApiResponse<MemberMessageResponse> deleteRefreshToken() {
        refreshTokenService.delete();
        return ApiResponse.success(new MemberMessageResponse("logout success"));
//        return ResponseEntity.ok()
//                .body(new MemberMessageResponse("logout success"));
    }

    // 회원(카카오) 정보 반환
    @Operation(summary="회원(카카오) 정보 반환 API", description = "헤더 액세스 토큰으로 요청 받아 회원(카카오) 정보를 반환합니다.")
    @GetMapping("/api/members")
    public ApiResponse<MemberResponse> findId(@RequestHeader("Authorization") String token){
        return ApiResponse.success(memberService.getMemberInfoByToken(token));
    }

    // 게스트 로그인
    @Operation(summary="게스트 로그인", description="게스트 계정은 서버에서 GET, DELETE만 허용하도록 설정함, DELETE는 로그아웃 용도 | 준비된 40개 계정 중 랜덤 로그인 " +
            "| 프로필 등록O")
    @GetMapping("/api/members/guest")
    public ApiResponse<FreePassResponse> loginGuest(){
        Random rd = new Random();
        String nickname = "guest" + (rd.nextInt(40)+1);
        return ApiResponse.success(memberService.loginFreePassMember(nickname));
    }

    // 회원탈퇴
    @Operation(summary="회원탈퇴", description="회원ID와 일치 확인후 회원탈퇴")
    @DeleteMapping("/api/members/signout/{memberId}")
    public ApiResponse<MemberSignOutResponse> signOut(@RequestHeader("Authorization") String token, @PathVariable Long memberId){
        MemberSignOutResponse memberSignOutResponse = memberService.signOut(token, memberId);
        refreshTokenService.delete();
        return ApiResponse.success(memberSignOutResponse);
    }

    // 개발용 기존회원 로그인
    @Operation(summary="(개발용)기존회원 로그인", description="미리 만들어둔 회원에 로그인, 토큰 받음")
    @GetMapping("/api/test/freepass/{nickname}")
    public ApiResponse<FreePassResponse> loginFreePass(@PathVariable String nickname){
        FreePassResponse freePassResponse = memberService.loginFreePassMember(nickname);
//        log.info("프리패스: " + freePassResponse.getNickname());
//        return ResponseEntity.ok().body(freePassResponse);
        return ApiResponse.success(freePassResponse);
    }



    // (개발용)자동으로 회원 생성하고 토큰 발급 -> 포스트맨으로만 검증함
//    @Operation(summary="(개발용)자동로그인", description="자동으로 회원 정보 생성하고 토큰 발급")
//    @GetMapping("/api/test/freepass")
//    public ResponseEntity<FreePassResponse> getFreePass(){
//        FreePassResponse freePassResponse = memberService.getFreePassMember();
////        log.info("프리패스: " + freePassResponse.getNickname());
////        log.info("프리패스: " + freePassResponse.getEmail());
////        log.info("프리패스: " + freePassResponse.getAccessToken());
////        log.info("프리패스: " + freePassResponse.getRefreshToken());
//        return ResponseEntity.ok()
//                .body(freePassResponse);
//    }

//    // (백엔드용)자동으로 회원 생성하고 토큰 발급 -> 포스트맨으로만 검증함
//    @Operation(summary="백엔드 테스트용")
//    @GetMapping("/api/test/allpass")
//    public ResponseEntity<?> getAllPass(){
//        ProfileResponse profileResponse = memberService.getFreePassProfile();
//        return ResponseEntity.ok()
//                .body(profileResponse);
//    }

}
