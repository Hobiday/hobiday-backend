package com.example.hobiday_backend.domain.follow.service;

import com.example.hobiday_backend.domain.follow.dto.response.FollowResponse;
import com.example.hobiday_backend.domain.follow.entity.Follow;
import com.example.hobiday_backend.domain.follow.repository.FollowRepository;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import com.example.hobiday_backend.domain.users.entity.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final ProfileRepository profileRepository;
    private final FollowRepository followRepository;

    public FollowResponse follow(Long followingId,Long followerId) {
        Profile following = profileRepository.findByUserId(followingId)
                        .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Profile follower = profileRepository.findByUserId(followerId)
                        .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        followRepository.save(Follow.builder()
                .following(following)
                .follower(follower)
                .build());

        return new FollowResponse(following.getUserId(), follower.getUserId());
    }

    public FollowResponse unfollow(Long followingId, Long followerId) {
        Follow follow = followRepository.findByFollowingAndFollower(followingId, followerId)
                .orElseThrow(() -> new NotFoundException(""));

        followRepository.delete(follow);

        Profile following = profileRepository.findByUserId(followingId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Long followingUser = following.getUserId();

        return new FollowResponse(followingUser, null);
    }
}
