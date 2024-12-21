package com.example.hobiday_backend.domain.follow.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FollowErrorCode {
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "FOLLOW-404", "해당 팔로우 관계를 찾을 수 없습니다."),
    FOLLOW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "FOLLOW-4001", "이미 팔로우 상태입니다."),
    FOLLOW_SELF(HttpStatus.BAD_REQUEST, "FOLLOW-4002", "자기 자신을 팔로우할 수 없습니다.");

    private final HttpStatus httpStatus; // HTTP 상태 코드
    private final String code;           // 오류 코드
    private final String message;        // 오류 메시지
}

