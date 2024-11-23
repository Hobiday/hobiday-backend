package com.example.hobiday_backend.domain.profile.service;

import com.example.hobiday_backend.domain.profile.dto.request.AddProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.request.UpdateProfileRequest;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileRegistrationResponse;
import com.example.hobiday_backend.domain.profile.dto.response.ProfileResponse;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import com.example.hobiday_backend.domain.users.entity.User;
import com.example.hobiday_backend.domain.users.repository.UserRepository;
import com.example.hobiday_backend.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final static String[] GENRES = {"연극", "무용", "대중무용", "서양음악", "한국음악", "대중음악", "복합", "서커스", "뮤지컬"};

    // 회원ID로 프로필 정보 반환
    public ProfileResponse getProfileByUserId(Long userid){
        Profile profile = profileRepository.findByUserId(userid)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        return ProfileResponse.builder()
                .profileId(profile.getId())
//                .userId(profile.getUserId()) // 방1
                .userId(profile.getUser().getId()) // 방2
                .profileName(profile.getProfileName())
                .profileEmail(profile.getProfileEmail())
//                .profileGenre(profile.getProfileGenre())
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
//                .profileGenre(profile.getProfileGenre())
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
                .profileGenre(getGenreToString(profile.profileGenre)) // 문자열 <- 리스트 변환해서 저장
                .profileActiveFlag(true)
                .build());
    }

    // 프로필 업데이트
    public ProfileResponse updateProfile(String token, UpdateProfileRequest updateProfileRequest) {
        Long userId = userService.getUserIdByToken(token);
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));

        // 사용자 일치 여부 확인
        if(!profile.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("프로필 수정 권한이 없습니다.");
        }
        profile = profileRepository.save(Profile.builder()
//                .user(profile.getUserId()) // 프로필의 User 필드는 처음 프로필 등록때 저장 받고 변경 불가능한 고유값이라 수정X
//                .profileEmail(profile.getProfileEmail()) // 이메일도 고유값이므로 수정X
                .profileName(profile.getProfileName())
                .profileGenre(!getGenreToString(updateProfileRequest.getProfileGenre()).isEmpty() ? getGenreToString(updateProfileRequest.getProfileGenre()) : profile.getProfileGenre())
                .profileIntroduction(updateProfileRequest.getProfileIntroduction() != null ? updateProfileRequest.getProfileIntroduction() : profile.getProfileIntroduction())
                .profileImageUrl(updateProfileRequest.getProfileImageUrl() != null ? updateProfileRequest.getProfileImageUrl() : profile.getProfileImageUrl())
                .build());

        profileRepository.save(profile);

        return ProfileResponse.builder()
//                .id(profile.getId()) // 프로필ID와 회원ID는 고유값이므로 수정X
//                .userId(profile.getUserId())
                .profileName(profile.getProfileName())
//                .profileEmail(profile.getProfileEmail())  // 이메일도 고유값이므로 수정X
                .profileGenre(getGenreToList(profile.getProfileGenre()))
                .profileIntroduction(profile.getProfileIntroduction())
                .profileImageUrl(profile.getProfileImageUrl())
                .build();
    }

    // 장르 타입 변환(리스트 -> 문자열 반환)
    // 장르순서: {"연극", "무용", "대중무용", "서양음악", "한국음악", "대중음악", "복합", "서커스", "뮤지컬"}
    public static String getGenreToString(List<String> genreList){
        StringBuilder strG = new StringBuilder("000000000");
        for (String genre:genreList){
            for (int i = 0; i < GENRES.length; i++) {
                if (genre.equals(GENRES[i])){
                    strG.setCharAt(i, '1');
                    break;
                }
            }
        }
        return strG.toString();
    }

    // 장르 타입 변환(문자열 -> 리스트)
    public static List<String> getGenreToList(String strG){
        List<String> genreList = new ArrayList<>();
        for (int i = 0; i < strG.length(); i++) {
            if(strG.charAt(i) == '1'){
                genreList.add(GENRES[i]);
            }
        }
        return genreList;
    }
}