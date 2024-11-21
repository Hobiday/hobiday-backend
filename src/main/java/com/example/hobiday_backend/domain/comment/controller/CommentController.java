package com.example.hobiday_backend.domain.comment.controller;

import com.example.hobiday_backend.domain.comment.dto.CommentReq;
import com.example.hobiday_backend.domain.comment.dto.CommentRes;
import com.example.hobiday_backend.domain.comment.service.CommentService;
import com.example.hobiday_backend.global.dto.SuccessRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{feedId}")
    public ResponseEntity<SuccessRes<CommentRes>> createComment(@PathVariable Long feedId,
                                                                @RequestBody CommentReq commentReq,
                                                                @AuthenticationPrincipal User user,
                                                                @RequestParam(required = false) Long parentCommentId) {
        CommentRes comment = commentService.createComment(feedId, commentReq, user, parentCommentId);
        return ResponseEntity.ok(SuccessRes.success(comment));
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<SuccessRes<List<CommentRes>>> getCommentsByFeedId(@PathVariable Long feedId,
                                                                            @AuthenticationPrincipal User user) {
        List<CommentRes> comments = commentService.getCommentsByFeedId(feedId, user);
        return ResponseEntity.ok(SuccessRes.success(comments));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessRes<CommentRes>> updateComment(@PathVariable Long id,
                                                                @RequestBody CommentReq commentReq,
                                                                @AuthenticationPrincipal User user) {
        CommentRes updatedComment = commentService.updateComment(id, commentReq, user);
        return ResponseEntity.ok(SuccessRes.success(updatedComment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessRes<Void>> deleteComment(@PathVariable Long id,
                                                          @AuthenticationPrincipal User user) {
        commentService.deleteComment(id, user);
        return ResponseEntity.ok(SuccessRes.success(null));
    }
}
