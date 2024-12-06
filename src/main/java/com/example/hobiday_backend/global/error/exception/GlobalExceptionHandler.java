package com.example.hobiday_backend.global.error.exception;

import com.example.hobiday_backend.domain.member.exception.MemberException;
import com.example.hobiday_backend.domain.perform.exception.PerformException;
import com.example.hobiday_backend.domain.profile.exception.ProfileException;
import com.example.hobiday_backend.global.dto.ErrorRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProfileException.class)
    public ResponseEntity<ErrorRes<Void>> handleProfileException(ProfileException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorRes<Void>> handleProfileException(MemberException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(PerformException.class)
    public ResponseEntity<ErrorRes<Void>> handleProfileException(PerformException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }
}
