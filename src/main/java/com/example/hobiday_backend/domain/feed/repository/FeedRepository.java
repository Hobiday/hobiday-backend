package com.example.hobiday_backend.domain.feed.repository;

import com.example.hobiday_backend.domain.feed.entity.Feed;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    // 최신순 조회
    List<Feed> findAllByOrderByCreatedTimeDesc();
    // 좋아요 순 조회
    List<Feed> findAllByOrderByLikeCountDesc();
    // 프로필 전체 피드 조회
    List<Feed> findAllByProfileIdOrderByCreatedTimeDesc(Long profileId);

    int countByProfile(Profile profile);
}
