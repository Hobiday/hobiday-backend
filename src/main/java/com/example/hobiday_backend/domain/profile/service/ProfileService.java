package com.example.hobiday_backend.domain.profile.service;

import com.example.hobiday_backend.domain.profile.dto.request.AddProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.request.UpdateProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileMessageResponse;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileResponse;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.exception.ProfileErrorCode;
import com.example.hobiday_backend.domain.profile.exception.ProfileException;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import com.example.hobiday_backend.domain.member.entity.Member;
import com.example.hobiday_backend.domain.member.repository.MemberRepository;
import com.example.hobiday_backend.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.example.hobiday_backend.domain.perform.util.GenreCasting.getGenreToList;
import static com.example.hobiday_backend.domain.perform.util.GenreCasting.getGenreToString;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;


    // 회원ID로 프로필 정보 반환
    public ProfileResponse getProfileByMemberId(Long memberId){
        Profile profile = profileRepository.findByMemberId(memberId)
                .orElseThrow(() ->new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));
        return ProfileResponse.builder()
                .profileId(profile.getId())
//                .userId(profile.getUserId()) // 방1
                .memberId(profile.getMember().getId()) // 방2
                .profileNickname(profile.getProfileNickname())
                .profileEmail(profile.getProfileEmail())
                .profileGenre(getGenreToList(profile.getProfileGenre()))
                .build();
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
//        log.info("dto 장르: " + addProfileRequest.profileGenre);
        profileRepository.save(Profile.builder()
//                .userId(userId) //방1
                .member(member) //방2
                .profileEmail(email)
                .profileNickname(addProfileRequest.profileNickname)
                .profileGenre(getGenreToString(addProfileRequest.profileGenre)) // 문자열 <- 리스트 변환해서 저장
                .build());
        return getProfileByMemberId(member.getId());
    }

    // 프로필 업데이트
    public ProfileResponse updateProfile(Long profileId, UpdateProfileRequest updateProfileRequest, Member member) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

        if (!profile.getMember().getId().equals(member.getId())) {
            throw new ProfileException(ProfileErrorCode.PROFILE_UPDATE_ACCESS_DENIED);
        }
        String profileGenre = updateProfileRequest.getProfileGenre() != null ?
                getGenreToString(updateProfileRequest.getProfileGenre()) : null;

        profile.updateProfile(
                updateProfileRequest.getProfileNickname(),
                updateProfileRequest.getProfileEmail(),
                profileGenre,
                updateProfileRequest.getProfileIntroduction(),
                updateProfileRequest.getProfileImageUrl()
        );
        return ProfileResponse.res(profileRepository.save(profile));
    };

// no use ============================================================================================================
//    // 프로필ID로 프로필 정보 반환
//    public ProfileResponse getProfile(Long profileId){
//        Profile profile = profileRepository.findById(profileId)
//                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));;
//        return ProfileResponse.builder()
//                .profileId(profile.getId())
//                .memberId(profile.getMember().getId())
//                .profileNickname(profile.getProfileNickname())
//                .profileEmail(profile.getProfileEmail())
////                .profileGenre(profile.getProfileGenre())
//                .build();
//    }

}