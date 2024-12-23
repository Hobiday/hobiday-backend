package com.example.hobiday_backend.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MemberSignOutResponse {
    private Long memberId;
    private String nickname;
    private String email;

    private Long profileId;
    private String profileNickname;
}
