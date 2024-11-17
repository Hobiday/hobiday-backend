package com.example.culture.domain.comment.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CommentReq {
    private String contents;
    private Long parentCommentId;
    private String profileName;
}
