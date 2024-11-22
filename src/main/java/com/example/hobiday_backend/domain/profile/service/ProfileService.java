package com.example.hobiday_backend.domain.profile.service;

import com.example.hobiday_backend.domain.profile.dto.request.AddProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileRegistrationResponse;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileResponse;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import com.example.hobiday_backend.domain.users.entity.User;
import com.example.hobiday_backend.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    // 회원ID로 프로필 정보 반환
    public ProfileResponse getProfileByUserId(Long userid){
        Profile profile = profileRepository.findByUserId(userid)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
        return ProfileResponse.builder()
                .profileId(profile.getId())
//                .userId(profile.getUserId()) // 방1
                .userId(profile.getUser().getId()) // 방2
                .profileName(profile.getProfileName())
                .profileEmail(profile.getProfileEmail())
                .profileGenre(profile.getProfileGenre())
                .build();
    }

    // 프로필ID로 프로필 정보 반환
    public ProfileResponse getProfile(Long profieId){
        Profile profile = profileRepository.findById(profieId).get();
        return ProfileResponse.builder()
                .profileId(profile.getId())
                .userId(profile.getUser().getId())
                .profileName(profile.getProfileName())
                .profileEmail(profile.getProfileEmail())
                .profileGenre(profile.getProfileGenre())
                .build();
    }

    // 프로필 등록 여부. 없는 경우도 알 수 있게 프로필 리턴 -> 아직 테스트 미실행
    public ProfileRegistrationResponse checkProfile(Long userId){
        Optional<Profile> profile = profileRepository.findByUserId(userId); // 회원ID로 만들어진 프로필DB 있는지 확인

        // 기존 회원일 경우
        if (profile.isPresent()) {
            return new ProfileRegistrationResponse(true);
        }

        // 신규 회원일 경우
        return new ProfileRegistrationResponse(false);
    }


    // 프로필 등록(온보딩 작성)
    @Transactional
    public Profile saveFirst(//Long userId, //방1
                             User user, //방2
                             AddProfileRequest profile){
//        String email = userRepository.findById(userId).get().getEmail(); //방1
        String email = user.getEmail(); //방2
        return profileRepository.save(Profile.builder()
//                .userId(userId) //방1
                .user(user) //방2
                .profileEmail(email)
                .profileName(profile.profileName)
                .profileGenre(profile.profileGenre)
                .profileActiveFlag(true)
                .build());
    }
}