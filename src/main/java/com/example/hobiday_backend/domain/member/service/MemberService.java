package com.example.hobiday_backend.domain.member.service;

import com.example.hobiday_backend.domain.member.dto.FreePassResponse;
import com.example.hobiday_backend.domain.member.dto.MemberResponse;
import com.example.hobiday_backend.domain.member.dto.MemberSignOutResponse;
import com.example.hobiday_backend.domain.member.entity.Member;
import com.example.hobiday_backend.domain.member.exception.MemberErrorCode;
import com.example.hobiday_backend.domain.member.exception.MemberException;
import com.example.hobiday_backend.domain.member.repository.MemberRepository;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.global.jwt.RefreshToken;
import com.example.hobiday_backend.global.jwt.RefreshTokenRepository;
import com.example.hobiday_backend.global.jwt.TokenProvider;
import com.example.hobiday_backend.global.oauth.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.example.hobiday_backend.global.oauth.OAuth2SuccessHandler.ACCESS_TOKEN_DURATION;
import static com.example.hobiday_backend.global.oauth.OAuth2SuccessHandler.REFRESH_TOKEN_DURATION;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 토큰으로 카카오 회원ID 반환
    public Long getMemberIdByToken(String token) {
        //token: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhc2R 에서 Bearer 뒤에만 사용해서 탐색
        String accessToken = token.split(" ")[1];
        return tokenProvider.getMemberId(accessToken);
    }

    // 토큰으로 회원 정보 반환
    public MemberResponse getMemberInfoByToken(String token) {
        Member member = findById(getMemberIdByToken(token));
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    public Member findById(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER__NOT_FOUND));
    }

    public Member findByEmail(String email){ // OAuth2에서 제공하는 정보는 유일 값이므로 해당 메서드로 회원 찾을 수 있음
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER__NOT_FOUND));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    // 회원탈퇴
    @Transactional
    public MemberSignOutResponse signOut(String token, Long memberId) {
        Member member = findById(getMemberIdByToken(token));
        Profile profile = member.getProfile();

        if (!Objects.equals(memberId, member.getId())){ // 접속중 사용자와 회원ID 일치 여부
            throw new MemberException(MemberErrorCode.MEMBER__NOT_ACCEPTABLE);
        }

        MemberSignOutResponse memberSignOutResponse
                = MemberSignOutResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileId(profile.getId())
                .profileNickname(profile.getProfileNickname())
                .build();

        memberRepository.delete(member);
        return memberSignOutResponse;
    }

    // 개발용 or 게스트 로그인
    public FreePassResponse loginFreePassMember(String nickname) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER__NOT_FOUND));
        new PrincipalDetails(member); // 회원을 현재의 UserDetails에 저장

        String refreshToken = tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
        saveRefreshToken(member.getId(), refreshToken); // 리프레시 토큰을 회원ID에 매칭해서 저장
        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);

        return FreePassResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }

    // 위에서 사용
    private void saveRefreshToken(Long memberId, String newRefreshToken) {
//        log.info("saveRefreshToken() 진입");
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId)
                .map(entity -> entity.update(newRefreshToken)) // 회원ID 대응되는 리프레시토큰 엔티티가 기존에 있으면 업데이트
                .orElse(new RefreshToken(memberId, newRefreshToken)); // 없으면 새로 생성
        refreshTokenRepository.save(refreshToken);
//        log.info("saveRefreshToken() 완료");
    }




    // no use ===============================================================================================
    // (개발환경용)자동으로 회원 생성하고 토큰 발급하는 메서드
//    public FreePassResponse getFreePassMember(){
//        String nickname = "FreePassUser" + (freePassNum++);
//        String email = nickname + "@freepass.com";
//        Member member = memberRepository.save(Member.builder()
//                .nickname(nickname)
//                .email(email)
//                .build());
//        new PrincipalDetails(member); // 회원을 현재의 UserDetails에 저장 => 필요 없나?
//
//        String refreshToken = tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
//        saveRefreshToken(member.getId(), refreshToken); // 리프레시 토큰을 회원ID에 매칭해서 저장
//        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
//
//        // (개발용:DB데이터 선입력)프로필도 자동 생성
//        profileRepository.save(Profile.builder()
//                .member(member)
//                .build());
//
//        return FreePassResponse.builder()
//                .id(member.getId())
//                .nickname(member.getNickname())
//                .email(member.getEmail())
//                .refreshToken(refreshToken)
//                .accessToken(accessToken)
//                .build();
//    }

    // (백엔드용)자동으로 회원, 토큰, 프로필 생성하는 메서드
//    public ProfileResponse getFreePassProfile(){
//        String nickname = "FreePassUser" + (freePassNum++);
//        String email = nickname + "@freepass.com";
//        Member member = memberRepository.save(Member.builder()
//                .nickname(nickname)
//                .email(email)
//                .build());
//        new PrincipalDetails(member); // 회원을 현재의 UserDetails에 저장 => 필요 없나?
//
//        String refreshToken = tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
//        saveRefreshToken(member.getId(), refreshToken); // 리프레시 토큰을 회원ID에 매칭해서 저장
////        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
//
//        // (개발용:DB데이터 선입력)프로필도 자동 생성
//        Profile profile = profileRepository.save(Profile.builder()
//                .member(member)
//                .profileEmail(member.getEmail())
//                .build());
//
//        return ProfileResponse.builder()
//                .profileId(profile.getId())
//                .memberId(profile.getId())
//                .profileNickname(member.getNickname())
//                .profileEmail(member.getEmail())
//                .build();
//    }


//    public UserResponse getUserResponse(String email) {
//        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
//        return UserResponse.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .nickname(user.getNickname())
//                .build();
//    }
//
//    public Long save(AddUserRequest dto){
//        return userRepository.save(User.builder()
//                .nickname(dto.getNickname())
//                .email(dto.getEmail())
////                .password(encoder.encode(dto.getPassword()))
//                .build()).getId();
//    }
}