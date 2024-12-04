package com.example.hobiday_backend.domain.feed.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FileUrlException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;

    public FileUrlException(FileUrlErrorCode fileUrlErrorCode) {
        super(fileUrlErrorCode.getMessage());
        this.httpStatus = fileUrlErrorCode.getHttpStatus();
        this.code= fileUrlErrorCode.getCode();
    }
}
