package com.example.hobiday_backend.domain.comment.dto;

import com.example.hobiday_backend.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Builder
public class CommentRes {
    private Long id;
    private String contents;
    private String profileName;
    private String profileImageUrl;
    // 상대 시간
    private String relativeTime;

    public static CommentRes from(Comment comment) {
        return CommentRes.builder()
                .id(comment.getId())
                .contents(comment.getContents())
                .profileName(comment.getProfile().getProfileNickname())
                .profileImageUrl(comment.getProfile().getProfileImageUrl())
                .relativeTime(calculateRelativeTime(comment.getCreatedTime()))
                .build();
    }

    // 상대 시간 계산 메서드
    private static String calculateRelativeTime(LocalDateTime createdAt) {
        Duration duration = Duration.between(createdAt, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return seconds + "초 전";
        } else if (seconds < 3600) {
            return (seconds / 60) + "분 전";
        } else if (seconds < 86400) {
            return (seconds / 3600) + "시간 전";
        } else {
            return (seconds / 86400) + "일 전";
        }
    }

}

