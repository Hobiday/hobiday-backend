package com.example.hobiday_backend.domain.users.service;

import com.example.hobiday_backend.domain.users.dto.AddUserRequest;
import com.example.hobiday_backend.domain.users.entity.User;
import com.example.hobiday_backend.domain.users.entity.UserResponse;
import com.example.hobiday_backend.domain.users.repository.UserRepository;
import com.example.hobiday_backend.global.jwt.TokenAuthenticationFilter;
import com.example.hobiday_backend.global.jwt.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;


@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

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

    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email){ // OAuth2에서 제공하는 정보는 유일 값이므로 해당 메서드로 유저 찾을 수 있음
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
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

    public UserResponse logout(String email) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        String accessToken = (String) authentication.getCredentials();
/*
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));*/
        var uri = "https://kapi.kakao.com/v1/user/logout";
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer "+accessToken);
        connection.setDoOutput(true);

        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("response code : " + responseCode);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        ObjectMapper ob = new ObjectMapper();

        return ob.readValue(sb.toString(), UserResponse.class);

    }

}
