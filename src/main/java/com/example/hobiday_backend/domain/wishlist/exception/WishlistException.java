package com.example.hobiday_backend.domain.wishlist.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WishlistException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;

    public WishlistException(WishlistErrorCode memberErrorCode) {
        super(memberErrorCode.getMessage());
        this.httpStatus = memberErrorCode.getHttpStatus();
        this.code = memberErrorCode.getCode();
    }
}
