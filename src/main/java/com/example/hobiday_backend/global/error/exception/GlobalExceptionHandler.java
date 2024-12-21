package com.example.hobiday_backend.global.error.exception;

import com.example.hobiday_backend.domain.comment.exception.CommentException;
import com.example.hobiday_backend.domain.feed.exception.FeedException;
import com.example.hobiday_backend.domain.feed.exception.FileUrlException;
import com.example.hobiday_backend.domain.feed.exception.HashTagException;
import com.example.hobiday_backend.domain.follow.exception.FollowException;
import com.example.hobiday_backend.domain.member.exception.MemberException;
import com.example.hobiday_backend.domain.perform.exception.PerformException;
import com.example.hobiday_backend.domain.profile.exception.ProfileException;
import com.example.hobiday_backend.domain.wishlist.exception.WishlistException;
import com.example.hobiday_backend.global.dto.ErrorRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FeedException.class)
    public ResponseEntity<ErrorRes<Void>> handleFeedException(FeedException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }
    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ErrorRes<Void>> handelCommentException(CommentException e) {
        log.info(e.getMessage(),e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }
    @ExceptionHandler(ProfileException.class)
    public ResponseEntity<ErrorRes<Void>> handleProfileException(ProfileException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorRes<Void>> handleMemberException(MemberException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(PerformException.class)
    public ResponseEntity<ErrorRes<Void>> handlePerformException(PerformException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(WishlistException.class)
    public ResponseEntity<ErrorRes<Void>> handleWishlistException(WishlistException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }

    // 신규: 파일 URL 예외 처리
    @ExceptionHandler(FileUrlException.class)
    public ResponseEntity<ErrorRes<Void>> handleFileUrlException(FileUrlException e) {
        log.info("FileUrlException: {}", e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }

    // 신규: 해시태그 예외 처리
    @ExceptionHandler(HashTagException.class)
    public ResponseEntity<ErrorRes<Void>> handleHashTagException(HashTagException e) {
        log.info("HashTagException: {}", e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(FollowException.class)
    public ResponseEntity<ErrorRes<Void>> handleFollowException(FollowException e) {
        log.info("HashTagException: {}", e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes<Void>> handleGeneralException(Exception e) {
        log.error("Unexpected Exception: {}", e.getMessage(), e);
        return ResponseEntity.status(500).body(ErrorRes.failure("UNEXPECTED_ERROR", "An unexpected error occurred."));
    }

}
