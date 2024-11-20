package com.example.hobiday_backend.domain.profile.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProfileResponse {
    private Long id;
    private Long userId;
    private String profileName;
    private String profileEmail;
    private String profileGenre;
    private Boolean profileActiveFlag;
    private String profileIntroduction;
    private String profilePhoto;
}
