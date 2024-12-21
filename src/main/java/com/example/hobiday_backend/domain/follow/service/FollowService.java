package com.example.hobiday_backend.domain.follow.service;

import com.example.hobiday_backend.domain.follow.dto.response.FollowMessageResponse;
import com.example.hobiday_backend.domain.follow.dto.response.FollowResponse;
import com.example.hobiday_backend.domain.follow.entity.Follow;
import com.example.hobiday_backend.domain.follow.exception.FollowErrorCode;
import com.example.hobiday_backend.domain.follow.exception.FollowException;
import com.example.hobiday_backend.domain.follow.repository.FollowRepository;
import com.example.hobiday_backend.domain.member.service.MemberService;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.exception.ProfileErrorCode;
import com.example.hobiday_backend.domain.profile.exception.ProfileException;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FollowService {
    private final FollowRepository followRepository;
    private final ProfileRepository profileRepository;
    private final MemberService memberService;

    public FollowMessageResponse toggleFollow(String token, Long targetProfileId) {
        Long memberId = memberService.getMemberIdByToken(token);
        Profile follower = profileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

        // 자기 자신을 팔로우하려는 경우를 방지
        if (follower.getId().equals(targetProfileId)) {
            throw new FollowException(FollowErrorCode.FOLLOW_SELF);
        }

        Profile target = profileRepository.findById(targetProfileId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

        Follow existingFollow = followRepository.findByFollowerAndFollowing(follower, target);

        if (existingFollow != null) {
            followRepository.delete(existingFollow);
            return new FollowMessageResponse("언팔로우 성공", false);
        } else {
            followRepository.save(new Follow(follower, target));
            return new FollowMessageResponse("팔로우 성공", true);
        }
    }

    public List<FollowResponse> getFollowingList(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

        List<Follow> following = followRepository.findAllByFollower(profile);

        if (following.isEmpty()) {
            log.info("팔로잉 목록이 비어 있습니다. profileId: {}", profileId);
        }

        return following.stream()
                .map(follow -> new FollowResponse(
                        follow.getFollowing(),
                        true
                ))
                .toList();
    }

    public List<FollowResponse> getFollowerList(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

        List<Follow> followers = followRepository.findAllByFollowing(profile);

        return followers.stream()
                .map(follow -> new FollowResponse(
                        follow.getFollower(),
                        true
                ))
                .toList();
    }
}
