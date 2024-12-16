package com.example.hobiday_backend.domain.profile.controller;

import com.example.hobiday_backend.domain.member.entity.Member;
import com.example.hobiday_backend.domain.member.repository.MemberRepository;
import com.example.hobiday_backend.domain.member.service.MemberService;
import com.example.hobiday_backend.domain.profile.dto.request.AddProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.request.UpdateProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileMessageResponse;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileRegistrationResponse;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileResponse;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import com.example.hobiday_backend.domain.profile.service.ProfileService;
import com.example.hobiday_backend.global.dto.ApiResponse;
import com.example.hobiday_backend.global.dto.file.PreSignedUrlRequest;
import com.example.hobiday_backend.global.dto.file.PresignedUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Profiles", description = "프로필 API")
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 프로필 등록(처음)하는 api
    @Operation(summary = "프로필 등록(온보딩 작성)", description = "토큰과 온보딩 작성한 데이터(닉네임, 장르)를 요청 받아 프로필 등록하고 반환합니다 ||" +
            "{\"연극\", \"무용\", \"대중무용\", \"서양음악\", \"한국음악\", \"대중음악\", \"복합\", \"서커스\", \"뮤지컬\"}")
    @PostMapping("/api/profiles/registration")
    public ApiResponse<ProfileResponse> join(@RequestHeader("Authorization") String token,
                                                @RequestBody AddProfileRequest addProfileRequest) {
        // 기존 프로필 없을 경우
//        Profile newProfile = profileService.saveFirst(userId, addProfileRequest); //방법1
        Long memberId = memberService.getMemberIdByToken(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        ProfileResponse newProfile = profileService.saveFirst(member, addProfileRequest);

        return ApiResponse.success(newProfile);
    }

    // 프로필등록 여부(O,X)
    @Operation(summary = "프로필 등록 여부 체크", description = "토큰을 요청 받아 온보딩 작성한 회원이면 true, 아니면 false 리턴합니다.")
    @GetMapping("/api/profiles/registration/check")
    public ApiResponse<ProfileRegistrationResponse> checkProfileRegistration(@RequestHeader("Authorization") String token) {
        Long memberId = memberService.getMemberIdByToken(token);
//        log.info("멤버ID:" + memberId);
        Optional<Profile> profileOptional = profileRepository.findByMemberId(memberId);
//        log.info("프로필여부:" + profileOptional.isPresent());

        // 있을때
        if (profileOptional.isPresent()) {
            return ApiResponse.success(new ProfileRegistrationResponse(true));
        }
        // 없을때
        return ApiResponse.success(new ProfileRegistrationResponse(false));
    }

    // 닉네임 중복 체크
    @Operation(summary = "닉네임 중복 여부", description = "닉네임 중복이면 overlapping | 중복 아니면 non-overlapping 문자열 리턴")
    @GetMapping("/api/profiles/registration/{nickname}")
    public ApiResponse<ProfileMessageResponse> isNicknameOverlap(@PathVariable String nickname) {
        return ApiResponse.success(profileService.isNicknameOverlap(nickname));
    }

    // 프로필 정보 반환
    @Operation(summary = "프로필 조회(by토큰)", description = "토큰을 요청 받아 프로필 정보를 반환합니다.")
    @GetMapping("/api/profiles/myprofile")
    public ApiResponse<ProfileResponse> getProfileByUserId(@RequestHeader("Authorization") String token){
        Long memberId = memberService.getMemberIdByToken(token);
        ProfileResponse profileResponse = profileService.getProfileByMemberId(memberId);
        return ApiResponse.success(profileResponse);
    }


    // 프로필 이미지 등록(수정)
    @Operation(summary = "프로필 이미지 등록(수정 포함)할 url 응답", description = "저장할 폴더명(prefix), 파일명(fileName) 요청해서 프로필 이미지 등록할 url을 응답 " +
            "| 동시에 프로필DB에는 이미지url 저장해놓음")
    @PostMapping("/api/profiles/image")
    public ApiResponse<PresignedUrlResponse> updateImage(@RequestHeader("Authorization") String token,
                                                         @RequestBody PreSignedUrlRequest presignedUrlRequest){
        Long memberId = memberService.getMemberIdByToken(token);
        return ApiResponse.success(profileService.updateImage(memberId, presignedUrlRequest));
    }

    // 프로필 수정
    @Operation(summary = "프로필 수정", description = "닉네임, 장르, 자기소개 수정 | 바꿀값만 key:value로 전송 | 이미지는 따로")
    @PutMapping("/api/profiles/update")
    public ApiResponse<ProfileResponse> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateProfileRequest updateProfileRequest) {

        Long memberId = memberService.getMemberIdByToken(token);
        return ApiResponse.success(profileService.updateProfile(memberId, updateProfileRequest));
    }

//    //    ============================= 백엔드 테스트용: 1.토큰으로 유저 확인 2.로그인->프로필 등록 =============================
//    @PostMapping("/api/profile")
//    public ResponseEntity<?> addProfile(@RequestBody AddProfileRequest addProfileRequest,
//                                        Principal principal, @RequestHeader("Authorization") String token) {
//        log.info("/api/profile 토큰:" + token);
//        Long memberId = memberService.getMemberIdByToken(token);
//        String name = memberRepository.findById(memberId).get().getEmail();
//        log.info("현재 로그인한 사용자명(액세스토큰): " + name);
//        log.info("현재 로그인한 사용자명(principal): " + principal.getName());
//
//        // 프로필에 저장
////        addProfileRequest.profileGenre =
//        profileService.saveFirst(memberService.findById(memberId), addProfileRequest);
//
//        log.info("입력 받은 닉네임: " + addProfileRequest.profileNickname);
//        log.info("입력 받은 장르: " + addProfileRequest.profileGenre);
//
////        addProfileRequest.profileName = userRepository.findByEmail(principal.getName()).get().getNickname();
////        User user = userRepository.findByEmail(principal.getName()).get();
//////        Profile profile = profileService.saveProfile(addProfileRequest, user);
////        Profile profile = profileService.saveFirst(user, addProfileRequest);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(null);
//    }



}