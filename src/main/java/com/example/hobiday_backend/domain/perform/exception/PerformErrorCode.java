package com.example.hobiday_backend.domain.perform.exception;

import com.example.hobiday_backend.global.domain.BaseError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PerformErrorCode implements BaseError {
    PERFORM_NOT_FOUND(HttpStatus.NOT_FOUND, "Perform_404", "공연 또는 시설을 찾을 수 없습니다"),
    PERFORM_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Perform_405", "해당 값을 비워서 요청할 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
