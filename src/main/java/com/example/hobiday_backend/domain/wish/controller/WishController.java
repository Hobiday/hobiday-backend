package com.example.hobiday_backend.domain.wish.controller;

import com.example.hobiday_backend.domain.member.service.MemberService;
import com.example.hobiday_backend.domain.wish.dto.WishRes;
import com.example.hobiday_backend.domain.wish.service.WishService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wish")
@Tag(name = "Wish", description = "위시리스트 API")
public class WishController {

    private final WishService wishService;
    private final MemberService memberService;

    @PostMapping("/{mt20id}")
    public ResponseEntity<WishRes> wish(@PathVariable Long mt20id, @RequestHeader String token){
        Long memberId = memberService.getMemberIdByToken(token);
        WishRes wishRes = wishService.wish(mt20id, memberId);
        return ResponseEntity.ok(wishRes);
    }
}
