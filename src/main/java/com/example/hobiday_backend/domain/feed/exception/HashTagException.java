package com.example.hobiday_backend.domain.feed.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HashTagException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String code;

    public HashTagException(HashTagErrorCode hashTagErrorCode) {
        super(hashTagErrorCode.getMessage());
        this.httpStatus = hashTagErrorCode.getHttpStatus();
        this.code= hashTagErrorCode.getCode();
    }
}
