package com.example.hobiday_backend.domain.users.controller;

import com.example.hobiday_backend.domain.profile.service.ProfileService;
import com.example.hobiday_backend.domain.users.entity.UserResponse;
import com.example.hobiday_backend.domain.users.repository.UserRepository;
import com.example.hobiday_backend.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/")
public class UserController {
    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final UserService userService;

    // 현재 로그인중인 사용자의 카카오 정보 조회 {조회 안될경우 만들어야하나?}
    @GetMapping("mypage")
    public ResponseEntity<UserResponse> getUser(Principal principal) {
        UserResponse userResponse = userService.getUserResponse(principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }
}
