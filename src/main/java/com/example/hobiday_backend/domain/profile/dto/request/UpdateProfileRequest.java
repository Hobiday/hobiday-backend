package com.example.hobiday_backend.domain.profile.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
    public String profileName;
    public String profileGenre;
    public String profileIntroduction;
    public String profilePhoto;
}
