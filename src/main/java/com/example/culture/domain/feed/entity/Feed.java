package com.example.culture.domain.feed.entity;

import com.example.culture.domain.comment.entity.Comment;
import com.example.culture.domain.feed.dto.FeedReq;
import com.example.culture.domain.like.entity.Like;
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
public class Feed extends TImeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 피드 내용
    @Column(nullable = false)
    private String content;

    // 프로필 이름
    private String profileName;

    // 유저
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 사진
    @Embedded
    private Picture picture;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HashTag> hashTags = new ArrayList<>();

    //좋아요
    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    private List<Like> likeList = new ArrayList<>();

    //댓글
    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();


    // dto의 값을 엔티티로 바꿔서 저장하기 위한 빌더
    @Builder
    public Feed(FeedReq feedReq, User user) {
        this.content = feedReq.getContent();
        this.picture = feedReq.getPicture();
        this.hashTags = feedReq.getHashTags();
        this.likeList = feedReq.getLikeList();
        this.commentList = feedReq.getCommentList();
        this.profileName = feedReq.getProfile().getProfileName();
        this.user = user;
    }


}
