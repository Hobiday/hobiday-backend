package com.example.hobiday_backend.domain.follow.repository;


import com.example.hobiday_backend.domain.follow.entity.Follow;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByFollowerAndFollowing(Profile follower, Profile following);
    boolean existsByFollowerAndFollowing(Profile follower, Profile following);
    List<Follow> findAllByFollower(Profile follower);
    List<Follow> findAllByFollowing(Profile following);
}