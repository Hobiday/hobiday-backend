package com.example.hobiday_backend.domain.wishlist.entity;

import com.example.hobiday_backend.domain.member.entity.Member;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.global.domain.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    private String mt20id;          // 공연상세ID
}
