package com.example.hobiday_backend.domain.users.service;

import com.example.hobiday_backend.domain.users.dto.AddUserRequest;
import com.example.hobiday_backend.domain.users.entity.User;
import com.example.hobiday_backend.domain.users.dto.UserResponse;
import com.example.hobiday_backend.domain.users.repository.UserRepository;
import com.example.hobiday_backend.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

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
        UserDetails findUser = (UserDetails) userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        if(findUser!=null) {
            return findUser;
        }
        return null;
    }

    // no use ===============================================================================================
    public UserResponse getUserResponse(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    public Long save(AddUserRequest dto){
        return userRepository.save(User.builder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
//                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }
}