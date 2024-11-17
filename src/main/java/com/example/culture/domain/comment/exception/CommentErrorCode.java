package com.example.culture.domain.comment.exception;

import com.example.culture.global.domain.BaseError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements BaseError {
    PARENT_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"COMMENT_404","부모 댓글을 찾을 수 없습니다");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
