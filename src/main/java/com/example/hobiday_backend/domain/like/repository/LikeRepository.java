package com.example.hobiday_backend.domain.like.repository;

import com.example.hobiday_backend.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // 특정 피드와 사용자의 좋아요 여부 조회
    Optional<Like> findByFeedIdAndUserId(Long feedId, Long userId);

    // 특정 피드에 대한 좋아요 갯수 조회
    int countByFeedId(Long feedId);
}
