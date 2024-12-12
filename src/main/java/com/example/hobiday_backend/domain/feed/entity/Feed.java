package com.example.hobiday_backend.domain.feed.entity;

import com.example.hobiday_backend.domain.comment.entity.Comment;
import com.example.hobiday_backend.domain.like.entity.Like;
import com.example.hobiday_backend.domain.perform.entity.Perform;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.global.domain.TimeStamped;
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
public class Feed extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 피드 내용
    @Column(nullable = false,length = 2200)
    private String content;

    // 주제
    @Column(nullable = false)
    private String topic;

    // 프로필
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    // 공연
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perform_id", nullable = false)
    private Perform perform;

    // 피드 사진
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<FeedFile> feedFiles = new ArrayList<>();

    // 해시 태그
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<HashTag> hashTags = new ArrayList<>();

    // 좋아요 연간관계 맵핑
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    // 좋아요 갯수 캐싱
    @Column(nullable = false)
    private int likeCount;

    //댓글
    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();


    // dto의 값을 엔티티로 바꿔서 저장하기 위한 빌더
    @Builder
    public Feed(String content,
                String topic,
                Profile profile,
                Perform perform) {
        this.content = content;
        this.profile = profile;
        this.topic = topic;
        this.perform = perform;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }

    // 업데이트 메서드
    public void update(String content, String topic, List<String> newFileUrls, List<String> newHashTags) {
        // 1. 내용 및 주제 수정
        this.content = content;
        this.topic = topic;

        // 2. FeedFiles 업데이트
        updateFeedFiles(newFileUrls);

        // 3. HashTags 업데이트
        updateHashTags(newHashTags);
    }

    private void updateFeedFiles(List<String> newFileUrls) {
        // 기존 파일과 비교
        List<FeedFile> filesToKeep = this.feedFiles.stream()
                .filter(feedFile -> newFileUrls.contains(feedFile.getFileUrl()))
                .toList();

        List<FeedFile> filesToAdd = newFileUrls.stream()
                .filter(url -> this.feedFiles.stream().noneMatch(file -> file.getFileUrl().equals(url)))
                .map(url -> FeedFile.builder().fileUrl(url).feed(this).build())
                .toList();

        // 기존 리스트 클리어 후 재구성
        this.feedFiles.clear();
        this.feedFiles.addAll(filesToKeep);
        this.feedFiles.addAll(filesToAdd);
    }

    private void updateHashTags(List<String> newHashTags) {
        // 기존 해시태그와 비교
        List<HashTag> tagsToKeep = this.hashTags.stream()
                .filter(hashTag -> newHashTags.contains(hashTag.getHashTag()))
                .toList();

        List<HashTag> tagsToAdd = newHashTags.stream()
                .filter(tag -> this.hashTags.stream().noneMatch(hash -> hash.getHashTag().equals(tag)))
                .map(tag -> HashTag.builder().hashTag(tag).feed(this).build())
                .toList();

        // 기존 리스트 클리어 후 재구성
        this.hashTags.clear();
        this.hashTags.addAll(tagsToKeep);
        this.hashTags.addAll(tagsToAdd);
    }
}
