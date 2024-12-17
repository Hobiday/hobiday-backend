package com.example.hobiday_backend.domain.wishlist.exception;

import com.example.hobiday_backend.global.domain.BaseError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WishlistErrorCode implements BaseError {
    WISH_NOT_FOUND(HttpStatus.NOT_FOUND, "WISH_404", "위시를 찾을 수 없습니다"),
    WISH_CONFLICT(HttpStatus.CONFLICT, "WISH_409_CONFLICT", "이미 등록된 위시입니다");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
