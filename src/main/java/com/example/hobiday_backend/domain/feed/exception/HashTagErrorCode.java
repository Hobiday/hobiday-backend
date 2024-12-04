package com.example.hobiday_backend.domain.feed.exception;

import com.example.hobiday_backend.global.domain.BaseError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HashTagErrorCode implements BaseError {
    EMPTY_HASH_TAG(HttpStatus.BAD_REQUEST, "HASH_001", "해시 태그는 비어 있을 수 없습니다");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
