package com.example.hobiday_backend.global.jwt;

import com.example.hobiday_backend.global.jwt.dto.CreateAccessTokenRequest;
import com.example.hobiday_backend.global.jwt.dto.CreateAccessTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    // 토큰을 발급 받는 api
    // Post 요청오면 토큰 서비스에서 리프레시 토큰을 기반으로 새로운 액세스 토큰을 만들어 준다.
    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken
            (@RequestBody CreateAccessTokenRequest request){
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
