package com.example.hobiday_backend.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentReq {
    private String contents;
    private Long parentCommentId;
    private String profileName;
}
