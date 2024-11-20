package com.example.hobiday_backend.domain.users.controller;//package com.example.hobiday_backend.domain.users.controller;

import com.example.hobiday_backend.domain.users.entity.UserResponse;
import com.example.hobiday_backend.domain.users.repository.UserRepository;
import com.example.hobiday_backend.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserViewController {
    private final UserRepository userRepository;
    private final UserService userService;
//
    //////     로그인시 oauthlogin(카카오로그인)으로 이동
    ////    @GetMapping("/login")
    ////    public String login(){
    ////        return "oauthLogin";
    ////    }

    @PostMapping("/logout")
    public ResponseEntity<UserResponse> logout(String email) throws Exception {
        UserResponse userResponse = userService.logout(email);
        return ResponseEntity.ok(userResponse);
    }
}
// 로그인은 프론트랑 연동완료가 아직 안됐으니 남겨둡시다
