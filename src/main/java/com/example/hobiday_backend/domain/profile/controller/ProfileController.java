package com.example.hobiday_backend.domain.profile.controller;

import com.example.hobiday_backend.domain.profile.dto.request.AddProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileRegistrationResponse;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileResponse;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService profileService;

    // 프로필 등록(처음)하는 api
    @PostMapping("/registration")
    public ResponseEntity<ProfileResponse> join(@RequestBody AddProfileRequest addProfileRequest,
                                                @RequestAttribute(value = "userId") Long userId) { // 여기서 userId는 user의 id
        // 기존 프로필 없을 경우
        Profile newProfile = profileService.saveFirst(userId, addProfileRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.getProfile(newProfile.getId())); // 생성한 프로필 정보 응답
    }

    // 프로필등록 여부(true, false) api
    @GetMapping("/registration/check")
    public ResponseEntity<ProfileRegistrationResponse> checkProfile(@RequestAttribute(value = "userId") Long userId) {
        ProfileRegistrationResponse profileRegistrationResponse = profileService.checkProfile(userId);

        // 있을때
        if (profileRegistrationResponse.isRegister()) {
            ResponseEntity.status(HttpStatus.OK).body(profileService.checkProfile(userId));
        }
        // 없을때
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(profileService.checkProfile(userId)); // 없다고 응답
    }

    // 프로필 조회 필요함
}
