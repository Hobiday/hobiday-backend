package com.example.hobiday_backend.domain.feed.repository;

import com.example.hobiday_backend.domain.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findAllByOrderByCreatedTimeDesc();
    List<Feed> findAllByOrderByLikeCountDesc();
    //List<Feed> findAllByProfileIdOrderByCreatedTimeDesc(Long profileId, Pageable pageable);
    List<Feed> findAllByProfileIdOrderByCreatedTimeDesc(Long profileId);
}