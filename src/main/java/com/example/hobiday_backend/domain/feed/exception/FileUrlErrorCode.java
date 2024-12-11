package com.example.hobiday_backend.domain.feed.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FileUrlErrorCode {
    EMPTY_FILE_URL(HttpStatus.BAD_REQUEST, "FILE_001", "파일 URL 리스트는 비어 있을 수 없습니다");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
