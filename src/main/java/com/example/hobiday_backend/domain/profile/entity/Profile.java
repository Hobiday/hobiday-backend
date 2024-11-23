package com.example.hobiday_backend.domain.profile.entity;

import com.example.hobiday_backend.domain.follow.entity.Follow;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private String profileIntroduction;
    private String profilePhoto;
    private Boolean profileActiveFlag;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followings = new ArrayList<>();

    @Builder
    public Profile(Long userId, String profileName, String profileGenre, String profileEmail, String profileIntroduction, String profilePhoto, Boolean profileActiveFlag) {
        this.userId = userId;
        this.profileName = profileName;
        this.profileEmail = profileEmail;
        this.profileGenre = profileGenre;
        this.profileIntroduction = profileIntroduction;
        this.profilePhoto = profilePhoto;
        this.profileActiveFlag = profileActiveFlag;
    }
}