//package com.example.hobiday_backend.domain.users.controller;//package com.example.hobiday_backend.domain.users.controller;
//import com.example.hobiday_backend.domain.member.entity.Member;
//import com.example.hobiday_backend.domain.member.repository.MemberRepository;
//import com.example.hobiday_backend.domain.member.service.MemberService;
//import com.example.hobiday_backend.domain.profile.dto.request.AddProfileRequest;
//import com.example.hobiday_backend.domain.profile.service.ProfileService;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//import java.security.Principal;
//
//@Tag(name="백엔드 테스트용 페이지")
//@Slf4j
//@RequiredArgsConstructor
//@Controller
//public class MemberViewController {
//    /* ======================== 백엔드 테스트용 페이지입니다(삭제X) ========================*/
//    private final MemberRepository memberRepository;
//    private final MemberService memberService;
//    private final ProfileService profileService;
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
//    @GetMapping("/home")
//    public String homes() {
//        return "home";
//    }
//
//
//    // 토큰으로 카카오 닉네임, 이메일 반환
//    @PostMapping("/checker")
//    public String home(Model model, @RequestHeader("Authorization") String token){
//        log.info("실행");
//        log.info("token: {}", token);
//        Long userId = memberService.getMemberIdByToken(token);
//        Member member = memberRepository.findById(userId).orElse(null);
//        String kakaoEmail = member.getEmail();
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
////     로그인 성공후 기존 회원이 아니면 회원가입 화면으로 이동(이전)
//    @GetMapping("/signForm")
//    public String getProfile(Model model) {
//
//        return "signList";
//    }

//    ============================= 백엔드 테스트용: 1.토큰으로 유저 확인 2.로그인->프로필 등록 =============================
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
//}
//
