package com.example.hobiday_backend.domain.profile.exception;

import com.example.hobiday_backend.domain.feed.exception.FeedErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProfileException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;

    public ProfileException(ProfileErrorCode profileErrorCode) {
        super(profileErrorCode.getMessage());
        this.httpStatus = profileErrorCode.getHttpStatus();
        this.code= profileErrorCode.getCode();
    }
}
