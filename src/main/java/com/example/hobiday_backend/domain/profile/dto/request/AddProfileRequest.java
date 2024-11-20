package com.example.hobiday_backend.domain.profile.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddProfileRequest {
    public String profileName;
    public String profileGenre;
    public String profileIntroduction;
    public String profilePhoto;
    // 이메일은 수정 못하도록 필드 선언 안함
}