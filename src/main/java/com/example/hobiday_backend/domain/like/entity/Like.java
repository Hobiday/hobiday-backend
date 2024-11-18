package com.example.hobiday_backend.domain.like.entity;

import com.example.hobiday_backend.domain.comment.entity.Comment;
import com.example.hobiday_backend.domain.feed.entity.Feed;
import com.example.hobiday_backend.domain.like.dto.LikeReq;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  /*  @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;*/

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    // 좋아요 갯수
    private int likeCount;

    @Builder
    public Like (LikeReq likeReq,Feed feed/*,User user*/,Comment comment) {
        this.feed = feed;
        this.comment = comment;
        this.likeCount = likeReq.getLikeCount();
    }

}
