package com.example.hobiday_backend.domain.follow.exception;

import com.example.hobiday_backend.domain.follow.repository.FollowRepository;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FollowException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;

    public FollowException(FollowErrorCode followErrorCode) {
        super(followErrorCode.getMessage());
        this.httpStatus = followErrorCode.getHttpStatus();
        this.code= followErrorCode.getCode();
    }
}
