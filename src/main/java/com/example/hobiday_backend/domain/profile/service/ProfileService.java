package com.example.hobiday_backend.domain.profile.service;

import com.example.hobiday_backend.domain.profile.dto.request.AddProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.request.UpdateProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileRegistrationResponse;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileResponse;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import com.example.hobiday_backend.domain.users.repository.UserRepository;
import com.example.hobiday_backend.global.jwt.TokenProvider;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    // users의 id로 만들어진 profile 반환
    public Profile findByUserId(Long id){
        Profile profile = profileRepository.findByUserId(id)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
        return profile;
    }

    // 회원가입 여부. 없는 경우도 알 수 있게 프로필 리턴 -> 아직 테스트 미실행
    public ProfileRegistrationResponse checkProfile(Long userId){
        Optional<Profile> profile = profileRepository.findByUserId(userId); // users의 id로 만들어진 프로필DB 있는지 확인

        // 기존 회원일 경우
        if (profile.isPresent()) {
            return new ProfileRegistrationResponse(true);
        }

        // 신규 회원일 경우
        return new ProfileRegistrationResponse(false);
    }


    // 회원가입 (처음 정보 입력)
    public Profile saveFirst(Long userId, AddProfileRequest profile){
        String email = userRepository.findById(userId).get().getEmail();
        return profileRepository.save(Profile.builder()
                .userId(userId)
                .profileEmail(email)
                .profileName(profile.profileName)
                .profileGenre(profile.profileGenre)
                .profileActiveFlag(true)
                .build());
    }

    // 프로필 아이디로 프로필DTO 반환
    public ProfileResponse getProfile(Long profileId){
        Profile profile = profileRepository.findById(profileId).get();
        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .profileName(profile.getProfileName())
                .profileEmail(profile.getProfileEmail())
                .profileGenre(profile.getProfileGenre())
                .build();
    }

    public ProfileResponse updateProfile(Long userId, UpdateProfileRequest updateProfileRequest) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));

        // 사용자 일치 여부 확인
        if(!profile.getUserId().equals(userId)) {
            throw new IllegalArgumentException("프로필 수정 권한이 없습니다.");
        }

       profile = profileRepository.save(Profile.builder()
                .userId(profile.getUserId())
                .profileEmail(profile.getProfileEmail())
                .profileName(profile.getProfileName())
                .profileGenre(updateProfileRequest.getProfileGenre() != null ? updateProfileRequest.getProfileGenre() : profile.getProfileGenre())
                .profileIntroduction(updateProfileRequest.getProfileIntroduction() != null ? updateProfileRequest.getProfileIntroduction() : profile.getProfileIntroduction())
                .profilePhoto(updateProfileRequest.getProfilePhoto() != null ? updateProfileRequest.getProfilePhoto() : profile.getProfilePhoto())
                .build());

        profileRepository.save(profile);

        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .profileName(profile.getProfileName())
                .profileEmail(profile.getProfileEmail())
                .profileGenre(profile.getProfileGenre())
                .profileIntroduction(profile.getProfileIntroduction())
                .profilePhoto(profile.getProfilePhoto())
                .build();
    }

}
