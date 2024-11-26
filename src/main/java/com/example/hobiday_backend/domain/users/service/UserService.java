package com.example.hobiday_backend.domain.users.service;

import com.example.hobiday_backend.domain.users.dto.FreePassResponse;
import com.example.hobiday_backend.domain.users.dto.PrincipalDetails;
import com.example.hobiday_backend.domain.users.entity.User;
import com.example.hobiday_backend.domain.users.repository.UserRepository;
import com.example.hobiday_backend.global.jwt.RefreshToken;
import com.example.hobiday_backend.global.jwt.RefreshTokenRepository;
import com.example.hobiday_backend.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.hobiday_backend.global.oauth.OAuth2SuccessHandler.ACCESS_TOKEN_DURATION;
import static com.example.hobiday_backend.global.oauth.OAuth2SuccessHandler.REFRESH_TOKEN_DURATION;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private static int freePassNum = 1;

    // 토큰 기반으로 카카오 회원 ID를 가져오는 메서드
    public Long getUserIdByToken(String token) {
        //token: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhc2R 에서 Bearer 뒤에만 사용해서 탐색
        String accessToken = token.split(" ")[1];
        return tokenProvider.getUserId(accessToken);
    }

    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    public User findByEmail(String email){ // OAuth2에서 제공하는 정보는 유일 값이므로 해당 메서드로 회원 찾을 수 있음
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    // (개발환경용)자동으로 회원 생성하고 토큰 발급하는 메서드
    public FreePassResponse getFreePassUser(){
        String nickname = "FreePassUser" + (freePassNum++);
        String email = nickname + "@freepass.com";
        User user = userRepository.save(User.builder()
                .nickname(nickname)
                .email(email)
                .build());
        new PrincipalDetails(user); // 회원을 현재의 UserDetails에 저장 => 필요 없나?

        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken); // 리프레시 토큰을 회원ID에 매칭해서 저장
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        return FreePassResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }
    private void saveRefreshToken(Long userId, String newRefreshToken) {
//        log.info("saveRefreshToken() 진입");
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken)) // 회원ID 대응되는 리프레시토큰 엔티티가 기존에 있으면 업데이트
                .orElse(new RefreshToken(userId, newRefreshToken)); // 없으면 새로 생성
        refreshTokenRepository.save(refreshToken);
//        log.info("saveRefreshToken() 완료");
    }




    // no use ===============================================================================================

//    public UserResponse getUserResponse(String email) {
//        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
//        return UserResponse.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .nickname(user.getNickname())
//                .build();
//    }
//
//    public Long save(AddUserRequest dto){
//        return userRepository.save(User.builder()
//                .nickname(dto.getNickname())
//                .email(dto.getEmail())
////                .password(encoder.encode(dto.getPassword()))
//                .build()).getId();
//    }
}