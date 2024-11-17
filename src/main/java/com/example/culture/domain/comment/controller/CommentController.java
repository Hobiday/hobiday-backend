package com.example.culture.domain.comment.controller;

import com.example.culture.domain.comment.dto.CommentReq;
import com.example.culture.domain.comment.dto.CommentRes;
import com.example.culture.domain.comment.service.CommentService;
import com.example.culture.global.dto.SuccessRes;
import com.example.culture.global.dto.UserDetailsImpl;
import com.example.culture.global.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/api/comment/{id}") // 여기서 id는 게시물의 id
    public ResponseEntity<SuccessRes<CommentRes>> getComments(@PathVariable Long id, @RequestBody CommentReq commentReq, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentRes comment = commentService.getComments(id, commentReq, userDetails.getUser());
        return ResponseEntity.ok(SuccessRes.success(comment));
    }
    // 댓글 수정
    @PutMapping
    // 댓글 삭제
    @DeleteMapping
    // 댓글 조화
    @GetMapping
}
