package com.example.hobiday_backend.domain.profile.exception;

import com.example.hobiday_backend.global.domain.BaseError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProfileErrorCode implements BaseError {
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "PROFILE_404", "프로필을 찾을 수 없습니다");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
