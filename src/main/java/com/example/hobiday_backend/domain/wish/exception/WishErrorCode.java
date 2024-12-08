package com.example.hobiday_backend.domain.wish.exception;

import com.example.hobiday_backend.global.domain.BaseError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WishErrorCode implements BaseError {

    WISH_NOT_FOUND(HttpStatus.NOT_FOUND, "WISH_404", "해당 게시물을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
