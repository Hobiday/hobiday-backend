package com.example.hobiday_backend.domain.comment.dto;

import com.example.hobiday_backend.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CommentRes {
    private Long id;
    private String contents;
    private String profileName;
    private String profileImageUrl;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private Integer likeCount;
    private boolean isLiked;
    private boolean isAuthor;
    private Long parentCommentId;
    private List<CommentRes> childCommentList;

    public static CommentRes from(Comment comment, boolean isLiked, boolean isAuthor) {
        return CommentRes.builder()
                .id(comment.getId())
                .contents(comment.getContents())
                .profileName(comment.getProfile().getName())
                .profileImageUrl(comment.getProfile().getProfileImageUrl())
                .createdTime(comment.getCreatedTime())
                .modifiedTime(comment.getModifiedTime())
                .likeCount(comment.getLikeList().size())
                .isLiked(isLiked)
                .isAuthor(isAuthor)
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .childCommentList(comment.getChildCommentList().stream()
                        .map(child -> CommentRes.from(child, false, false))
                        .collect(Collectors.toList()))
                .build();
    }
}
