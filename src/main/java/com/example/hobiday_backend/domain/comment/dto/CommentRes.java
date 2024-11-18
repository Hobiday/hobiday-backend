package com.example.hobiday_backend.domain.comment.dto;

import com.example.hobiday_backend.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentRes {
    private Long id;
    private String contents;
    private String username;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private Integer likeCount;
    private List<CommentRes> childCommentList;

    // from 메서드 추가
    @Builder
    public CommentRes(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        /*this.username = comment.getUser().getUsername();*/
        this.createdTime = comment.getCreatedTime();
        this.modifiedTime = comment.getModifiedTime();
        this.likeCount = (int) comment.getLikeList().stream().count();
        //this.childCommentList = comment.getChildCommentList().stream().map(CommentRes::from).toList();
    }

}
