package com.example.hobiday_backend.domain.profile.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;        // users의 id
    private String profileName;
    private String profileEmail;
    private String profileGenre;
    private Boolean profileActiveFlag;
    private String profileIntroduction;
    private String profilePhoto;

    @Builder
    public Profile(Long userId, String profileName, String profileGenre, String profileEmail, Boolean profileActiveFlag, String profileIntroduction, String profilePhoto) {
        this.userId = userId;
        this.profileName = profileName;
        this.profileEmail = profileEmail;
        this.profileGenre = profileGenre;
        this.profileActiveFlag = profileActiveFlag;
        this.profileIntroduction = profileIntroduction;
        this.profilePhoto = profilePhoto;
    }
}