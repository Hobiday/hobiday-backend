package com.example.hobiday_backend.domain.users.controller;//package com.example.hobiday_backend.domain.users.controller;
//import com.example.hobiday_backend.domain.users.entity.User;
//import com.example.hobiday_backend.domain.users.repository.UserRepository;
//import com.example.hobiday_backend.domain.users.service.UserService;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//@Tag(name="백엔드 테스트용 페이지")
//@Slf4j
//@RequiredArgsConstructor
//@Controller
//public class UserViewController {
//    /* ======================== 백엔드 테스트용 페이지입니다(삭제X) ========================*/
//    private final UserRepository userRepository;
//    private final UserService userService;
//
//    // 로그인시 oauthlogin(카카오로그인)으로 이동
//    @GetMapping("/login")
//    public String login(){
//        return "oauthLogin";
//    }
//
//    // 로그인후 이동 페이지(프로필 등록 확인) | profile.js 켜야함
//    @GetMapping("/registration-form")
//    public String getProfile() {
//        return "signList";
//    }
//
//    // 로그인후 이동 페이지(토큰, 로그아웃 확인)
////    @GetMapping("/registration-form")
////    public String tokenSender(){
////
////        return "tokenSender";
////    }
//
//    // 토큰으로 카카오 닉네임, 이메일 반환
//    @PostMapping("/checker")
//    public String home(Model model, @RequestHeader("Authorization") String token){
//        log.info("실행");
//        log.info("token: {}", token);
//        Long userId = userService.getUserIdByToken(token);
//        User user = userRepository.findById(userId).orElse(null);
//        String kakaoEmail = user.getEmail();
//        log.info("kakaoEmail: " + kakaoEmail);
//        model.addAttribute("kakaoEmail", kakaoEmail);
//        return "nickEmail";
//    }
//
//    //
//    @GetMapping("/is-logout")
//    public String tester(){
//        return "logoutTest";
//    }
//
//}
//
