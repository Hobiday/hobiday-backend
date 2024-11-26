package com.example.hobiday_backend.domain.follow.controller;

import com.example.hobiday_backend.domain.follow.service.FollowService;
import com.example.hobiday_backend.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final TokenProvider tokenProvider;

    @GetMapping("/follow")
    public ResponseEntity<String>  follow(@RequestParam(value = "token") String token, Long followingId) {
        Long followerId = tokenProvider.getUserId(token);

        try {
            followService.follow(followingId, followerId);
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("fail");
        }
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollow(@RequestParam(value = "token") String token, @RequestParam Long followingId) {
        Long followerId = tokenProvider.getUserId(token);

        try {
            followService.unfollow(followingId, followerId);
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("fail");
        }
    }

}
