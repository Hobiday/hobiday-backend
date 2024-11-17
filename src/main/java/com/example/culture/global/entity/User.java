package com.example.culture.global.entity;

import com.example.culture.global.dto.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 로그인용 ID 또는 이메일

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // 사용자 권한 (Enum 사용)

    @Builder
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}
