package com.example.hobiday_backend.domain.follow.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FollowErrorCode {
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "FOLLOW_404", "팔로우 관계가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
