package com.example.hobiday_backend.domain.perform.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PerformException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;

    public PerformException(PerformErrorCode memberErrorCode) {
        super(memberErrorCode.getMessage());
        this.httpStatus = memberErrorCode.getHttpStatus();
        this.code = memberErrorCode.getCode();
    }
}
