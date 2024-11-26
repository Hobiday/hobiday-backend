package com.example.hobiday_backend.domain.members.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FreePassResponse {
    private Long id;
    private String nickname;
    private String email;
    private String accessToken;
    private String refreshToken;
}