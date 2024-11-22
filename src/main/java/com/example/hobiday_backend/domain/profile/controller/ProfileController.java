package com.example.hobiday_backend.domain.profile.controller;

import com.example.hobiday_backend.domain.profile.dto.request.AddProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileRegistrationResponse;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileResponse;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.service.ProfileService;
import com.example.hobiday_backend.domain.users.dto.UserResponse;
import com.example.hobiday_backend.domain.users.repository.UserRepository;
import com.example.hobiday_backend.domain.users.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;
    private final UserRepository userRepository;

    // 프로필 등록(처음)하는 api
    @Tag(name="프로필 등록(온보딩 작성) API", description = "온보딩 작성한 데이터(닉네임, 장르)를 요청 받아 프로필 등록하고 반환합니다" +
            "\n!닉네임 중복은 아직 안만들었음" +
            "\n!장르는 100000000 처럼 9개 장르코드 순서대로 선택/미선택 문자열")
    @PostMapping("/api/profiles/registration")
    public ResponseEntity<ProfileResponse> join(@RequestBody AddProfileRequest addProfileRequest,
                                                @RequestAttribute(value = "userId") Long userId) {
        // 기존 프로필 없을 경우
//        Profile newProfile = profileService.saveFirst(userId, addProfileRequest); //방법1
        Profile newProfile = profileService.saveFirst(userRepository.findById(userId).get(), addProfileRequest); //방법2

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.getProfile(newProfile.getId())); // 생성한 프로필 정보 응답
    }

    // 프로필등록 여부(O,X) api
    @Tag(name="프로필 등록(온보딩 작성) 여부 체크 API", description = "회원ID를 전달 받아 온보딩 작성한 회원이면 true, 아니면 false 리턴합니다.")
    @GetMapping("/api/profiles/registration/check")
    public ResponseEntity<ProfileRegistrationResponse> checkProfileRegistration(@RequestAttribute(value = "userId") Long userId) {
        ProfileRegistrationResponse profileRegistrationResponse = profileService.checkProfile(userId);

        // 있을때
        if (profileRegistrationResponse.isRegister()) {
            ResponseEntity.status(HttpStatus.OK).body(profileService.checkProfile(userId));
        }
        // 없을때
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(profileService.checkProfile(userId)); // 없다고 응답
    }

    // 프로필 정보 반환
    @Tag(name="프로필 조회(by토큰) API", description = "액세스 토큰으로 요청 받아 프로필 정보를 반환합니다.")
    @GetMapping("/api/profiles/myprofile")
    public ResponseEntity<ProfileResponse> getProfileByUserId(@RequestHeader("Authorization") String token){
        Long userId = userService.getUserIdByToken(token);
        ProfileResponse profileResponse = profileService.getProfileByUserId(userId);
        if (profileResponse == null) { // 프로필 작성이 없는 회원이면
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(profileResponse);
        }

        // 있으면 정보 리턴
        return ResponseEntity.status(HttpStatus.OK).body(profileResponse);
    }

    // ============================= 백엔드 테스트용: 로그인->프로필 등록 =============================
//    @PostMapping("/api/profile")
//    public ResponseEntity<?> addProfile(@RequestBody AddProfileRequest addProfileRequest,
//                                        Principal principal) {
//        log.info("현재 로그인한 사용자명(principal): " + principal.getName()); // 둘 중 하나 선택
//
//        addProfileRequest.profileName = userRepository.findByEmail(principal.getName()).get().getNickname();
//        User user = userRepository.findByEmail(principal.getName()).get();
////        Profile profile = profileService.saveProfile(addProfileRequest, user);
//        Profile profile = profileService.saveFirst(user, addProfileRequest);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(profile.getProfileName());
//    }
}