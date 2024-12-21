package com.example.hobiday_backend.domain.profile.service;

import com.example.hobiday_backend.domain.feed.repository.FeedRepository;
import com.example.hobiday_backend.domain.follow.repository.FollowRepository;
import com.example.hobiday_backend.domain.member.entity.Member;
import com.example.hobiday_backend.domain.profile.dto.request.AddProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.request.UpdateProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileMessageResponse;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileResponse;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.exception.ProfileErrorCode;
import com.example.hobiday_backend.domain.profile.exception.ProfileException;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import com.example.hobiday_backend.global.dto.file.PreSignedUrlRequest;
import com.example.hobiday_backend.global.dto.file.PresignedUrlResponse;
import com.example.hobiday_backend.global.s3.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hobiday_backend.domain.perform.util.GenreCasting.getGenreToString;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final FileService fileService;
    private final FeedRepository feedRepository;
    private final FollowRepository followRepository;

    // 회원ID로 프로필 정보 반환
    public ProfileResponse getProfileByMemberId(Long memberId){
        Profile profile = profileRepository.findByMemberId(memberId)
                .orElseThrow(() ->new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));
        int totalFeedCount = feedRepository.countByProfile(profile);
        int followerCount = followRepository.countByFollowing(profile);
        int followingCount = followRepository.countByFollower(profile);
        return ProfileResponse.of(profile, totalFeedCount, followerCount, followingCount);
    }

    // 프로필ID로 프로필 조회
    public ProfileResponse getProfileByProfileId(Long profileId){
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() ->new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));
        int totalFeedCount = feedRepository.countByProfile(profile);
        int followerCount = followRepository.countByFollowing(profile);
        int followingCount = followRepository.countByFollower(profile);
        return ProfileResponse.of(profile, totalFeedCount, followerCount, followingCount);
    }

    // 닉네임 중복 여부
    public ProfileMessageResponse isNicknameOverlap(String nickname){
        if (profileRepository.findByProfileNickname(nickname).isPresent()) { // 존재하는 닉네임이면
            return new ProfileMessageResponse("overlapping");
        }
        return new ProfileMessageResponse("non-overlapping");
    }

    // 프로필 등록(온보딩 작성)
    @Transactional
    public ProfileResponse saveFirst(//Long userId, //방1
                                     Member member, //방2
                                     AddProfileRequest addProfileRequest){
//        String email = userRepository.findById(userId).get().getEmail(); //방1
        String email = member.getEmail(); //방2
        if(profileRepository.findByMemberId(member.getId()).isPresent()){
            throw new ProfileException(ProfileErrorCode.PROFILE_CONFLICT);
        }
//        log.info("dto 장르: " + addProfileRequest.profileGenre);
        Profile profile = profileRepository.save(Profile.builder()
//                .userId(userId) //방1
                .member(member) //방2
                .profileEmail(email)
                .profileNickname(addProfileRequest.profileNickname)
                .profileGenre(getGenreToString(addProfileRequest.profileGenre)) // 문자열 <- 리스트 변환해서 저장
                .build());
        return ProfileResponse.from(profile);
    }

    // 프로필 업데이트
    @Transactional
    public ProfileResponse updateProfile(Long memberId, UpdateProfileRequest updateProfileRequest) {
        Profile profile = profileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));
        profile.updateProfile(updateProfileRequest);
        return ProfileResponse.from(profile);
    }

    // 프로필 수정
    @Transactional
    public PresignedUrlResponse updateImage(PreSignedUrlRequest presignedUrlRequest) {
        PresignedUrlResponse presignedUrlResponse = fileService.getUploadPresignedUrl(presignedUrlRequest.getPrefix(),
                presignedUrlRequest.getFileName());

        return presignedUrlResponse;
    }

}