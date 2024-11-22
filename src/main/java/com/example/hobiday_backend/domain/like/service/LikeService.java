package com.example.hobiday_backend.domain.like.service;

import com.example.hobiday_backend.domain.feed.entity.Feed;
import com.example.hobiday_backend.domain.feed.repository.FeedRepository;
import com.example.hobiday_backend.domain.like.dto.LikeRes;
import com.example.hobiday_backend.domain.like.entity.Like;
import com.example.hobiday_backend.domain.like.repository.LikeRepository;
import com.example.hobiday_backend.domain.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final FeedRepository feedRepository;

    public LikeRes toggleLike(Long feedId, User user) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("피드를 찾을 수 없습니다."));

        Optional<Like> existingLike = likeRepository.findByFeedIdAndUserId(feedId, user.getId());
        boolean isLiked;

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            feed.decrementLikeCount();
            isLiked = false;
        } else {
            Like newLike = new Like(feed, user);
            likeRepository.save(newLike);
            feed.incrementLikeCount();
            isLiked = true;
        }

        feedRepository.save(feed);

        return LikeRes.builder()
                .isLiked(isLiked)
                .likeCount(feed.getLikeCount())
                .build();
    }
}
