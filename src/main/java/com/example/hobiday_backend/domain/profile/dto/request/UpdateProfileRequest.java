package com.example.hobiday_backend.domain.profile.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdateProfileRequest {
    private String profileNickname;
    private String profileEmail;
    private List<String> profileGenre;
    private String profileIntroduction;
    private String profileImageUrl;

}