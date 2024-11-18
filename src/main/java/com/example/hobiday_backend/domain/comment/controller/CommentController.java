package com.example.hobiday_backend.domain.comment.controller;

import com.example.hobiday_backend.domain.comment.dto.CommentReq;
import com.example.hobiday_backend.domain.comment.dto.CommentRes;
import com.example.hobiday_backend.domain.comment.service.CommentService;
import com.example.hobiday_backend.global.dto.SuccessRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/api/comment/{id}") // 여기서 id는 게시물의 id
    public ResponseEntity<SuccessRes<CommentRes>> getComments(@PathVariable Long id, @RequestBody CommentReq commentReq /*@AuthenticationPrincipal UserDetailsImpl userDetails*/) {
        CommentRes comment = commentService.getComments(id, commentReq/*, userDetails.getUser()*/);
        return ResponseEntity.ok(SuccessRes.success(comment));
    }
    // 댓글 수정

    // 댓글 삭제

    // 댓글 조화

}
