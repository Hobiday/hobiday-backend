package com.example.hobiday_backend.domain.wish.exception;

import org.springframework.http.HttpStatus;

public class WishException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;

    public WishException(WishErrorCode wishErrorCode) {
        super(wishErrorCode.getMessage());
        this.httpStatus = wishErrorCode.getHttpStatus();
        this.code = wishErrorCode.getCode();
    }

}
