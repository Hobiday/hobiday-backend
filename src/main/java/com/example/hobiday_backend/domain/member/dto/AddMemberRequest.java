package com.example.hobiday_backend.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {
    private String nickname;
    private String email;
    private String password;

}
