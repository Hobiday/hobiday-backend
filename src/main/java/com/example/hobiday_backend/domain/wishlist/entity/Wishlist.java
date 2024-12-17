package com.example.hobiday_backend.domain.wishlist.entity;

import com.example.hobiday_backend.global.domain.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = "Wishlist",
        uniqueConstraints={ // 복합키 설정 (중복 저장 불가)
        @UniqueConstraint(
                columnNames={"profile_id", "mt20id"}
        )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Wishlist extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long profileId;
    private String mt20id;          // 공연상세ID
    private String genre;
    private String performName;           // 공연명
    private String performState;        // 공연상태 (예정, 중, 완료)
    private String placeName;         // 공연시설명 (피가로아트홀)
    private String area;            // 지역명 (서울)
    private String poster;          // 공연포스터 경로 (url)

    @Builder
    public Wishlist(Long profileId, String mt20id, String genre, String performName, String performState, String placeName, String area, String poster) {
        this.profileId = profileId;
        this.mt20id = mt20id;
        this.genre = genre;
        this.performName = performName;
        this.performState = performState;
        this.placeName = placeName;
        this.area = area;
        this.poster = poster;
    }
}
