package com.example.hobiday_backend.domain.follow.repository;

import com.example.hobiday_backend.domain.follow.entity.Follow;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowingAndFollower(Profile followingId, Profile followerId);
}
