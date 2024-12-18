package com.example.hobiday_backend.domain.profile.entity;

import com.example.hobiday_backend.domain.follow.entity.Follow;
import com.example.hobiday_backend.domain.like.entity.Like;
import com.example.hobiday_backend.domain.member.entity.Member;
import com.example.hobiday_backend.domain.profile.dto.request.UpdateProfileRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.example.hobiday_backend.domain.perform.util.GenreCasting.getGenreToString;


@Table(name = "profiles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    private Long userId; // 방법1
    @OneToOne(fetch = FetchType.LAZY) // 방법2
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    // FROM profile INNER JOIN users ON profile.user_id = users.id
    private Member member;

    @Column(length=20, unique = true)
    private String profileNickname;
    private String profileEmail;

    @Column(length=20)
    private String profileGenre;

    @Column(length=500)
    private String profileIntroduction;

    @Column(nullable = true)
    private String profileImageUrl;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followings = new ArrayList<>();

    // 좋아요 연간관계 맵핑
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @Builder
    public Profile(//Long userId, //방법1
                   Member member, // 방법2
                   String profileNickname, String profileGenre, String profileEmail,
                   String profileIntroduction, String profileImageUrl) {
//        this.userId = userId; //방법1
        this.member = member;
        this.profileNickname = profileNickname;
        this.profileEmail = profileEmail;
        this.profileGenre = profileGenre;
        this.profileIntroduction = profileIntroduction;
        this.profileImageUrl = profileImageUrl;
    }

    public void updateImage(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }


    public void updateProfile(UpdateProfileRequest updateProfileRequest) {
        if(updateProfileRequest.getProfileGenre()!=null){
            this.profileGenre = getGenreToString(updateProfileRequest.getProfileGenre());
        }
        if(updateProfileRequest.getProfileNickname()!=null){
            this.profileNickname = updateProfileRequest.getProfileNickname();
        }
        if(updateProfileRequest.getProfileIntroduction()!=null){
            this.profileIntroduction = updateProfileRequest.getProfileIntroduction();
        }
        if(updateProfileRequest.getProfileImageFilePath()!=null && !updateProfileRequest.getProfileImageFilePath().equals("")){
//            String[] urls = updateProfileRequest.getProfileImageUrl().split("/");
//            urls[2] = "cdn.hobiday.site";
//            String cdnUrl = "https://";
//            for (int i = 2; i < urls.length; i++) {
//                cdnUrl += urls[i] + "/";
//            }
//            this.profileImageUrl = cdnUrl.substring(0, cdnUrl.length() - 1);
//            this.profileImageUrl = updateProfileRequest.getProfileImageUrl();
            this.profileImageUrl = "https://cdn.hobiday.site/" + updateProfileRequest.getProfileImageFilePath();
        }
    }
}