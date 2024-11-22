package com.example.hobiday_backend.domain.like.controller;

import com.example.hobiday_backend.domain.like.dto.LikeRes;
import com.example.hobiday_backend.domain.users.entity.User;
import com.example.hobiday_backend.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final UserService userService;

    @PostMapping("/{feedId}")
    public ResponseEntity<LikeRes> toggleLike(@PathVariable Long feedId, @RequestHeader String token) {
        Long userId = userService.getUserIdByToken(token);
        User user = userService.findById(userId);
        LikeResponse response = likeService.toggleLike(feedId, user);
        return ResponseEntity.ok(response);
    }
}
