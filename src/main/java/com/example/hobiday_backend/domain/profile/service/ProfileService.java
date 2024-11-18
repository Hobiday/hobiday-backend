package com.example.hobiday_backend.domain.profile.service;

import com.example.hobiday_backend.domain.profile.dto.request.AddProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileRegistrationResponse;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileResponse;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import com.example.hobiday_backend.domain.users.repository.UserRepository;
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
    public ProfileResponse getProfile(Long profieId){
        Profile profile = profileRepository.findById(profieId).get();
        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .profileName(profile.getProfileName())
                .profileEmail(profile.getProfileEmail())
                .profileGenre(profile.getProfileGenre())
                .build();
    }
}
