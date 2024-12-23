package com.example.hobiday_backend.domain.member.exception;

import com.example.hobiday_backend.global.domain.BaseError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseError {
    MEMBER__NOT_FOUND(HttpStatus.NOT_FOUND, "Member_404", "회원을 찾을 수 없습니다"),
    MEMBER__NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "Member_406", "현재 접속한 사용자와 회원ID가 일치하지 않습니다");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
