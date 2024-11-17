package com.example.culture.domain.comment.entity;

import com.example.culture.domain.comment.dto.CommentReq;
import com.example.culture.domain.feed.entity.Feed;
import com.example.culture.domain.like.entity.Like;
import com.example.culture.domain.profile.entity.Profile;
import com.example.culture.global.domain.TImeStamped;
import com.example.culture.global.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends TImeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 댓글 내용
    @Column(nullable = false, length = 2000)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    // 누가 작성하는 댓글인지 알아야 되니까 사용자 필요
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 좋아요 리스트
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Like> likeList = new ArrayList<>();

    // 부모 댓글
    private Long parentCommentId;

    // 프로필 이름
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    // 오름차순 정렬
    @OrderBy("createdTime asc")
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
    private List<Comment> childCommentList = new ArrayList<>();

    @Builder
    public Comment(String contents, Feed feed, User user,Long parentCommentId) {
        this.contents = contents;
        this.feed = feed;
        this.user = user;
        this.parentCommentId = parentCommentId;
    }

    public void addCommentTOFeed(Comment comment) {
        feed.getCommentList().add(comment);
    }

    public void addChildComment(Comment child) {
        this.getChildCommentList().add(child);
    }

    // 댓글 수정 추가해야 함


}
