package com.example.hobiday_backend.domain.profile.entity;

import com.example.hobiday_backend.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    private Long userId; // 방법1
    @OneToOne // 방법2
    @JoinColumn(name="user_id")
    private User user;

    @Column(length=20)
    private String profileName;
    private String profileEmail;

    @Column(length=20)
    private String profileGenre;

    @Column(length=500)
    private String profileIntroduction;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean profileActiveFlag; // 프로필 등록 여부

    @Column(nullable = true)
    private String profileImageUrl;

    @Builder
    public Profile(//Long userId, //방법1
                   User user, // 방법2
                   String profileName, String profileGenre, String profileEmail, Boolean profileActiveFlag, String profileIntroduction) {
        // String profilePhoto
//        this.userId = userId; //방법1
        this.user = user;
        this.profileName = profileName;
        this.profileEmail = profileEmail;
        this.profileGenre = profileGenre;
        this.profileActiveFlag = profileActiveFlag;
        this.profileIntroduction = profileIntroduction;
//        this.profilePhoto = profilePhoto;
    }
}