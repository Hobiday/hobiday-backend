package com.example.hobiday_backend.domain.wish.service;

import com.example.hobiday_backend.domain.perform.entity.Perform;
import com.example.hobiday_backend.domain.perform.exception.PerformErrorCode;
import com.example.hobiday_backend.domain.perform.exception.PerformException;
import com.example.hobiday_backend.domain.perform.repository.PerformRepository;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import com.example.hobiday_backend.domain.wish.dto.WishRes;
import com.example.hobiday_backend.domain.wish.entity.Wish;
import com.example.hobiday_backend.domain.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WishService {
    private final PerformRepository performRepository;
    private final ProfileRepository profileRepository;
    private final WishRepository wishRepository;

    public WishRes wish(Long mt20id, Long memberId) {
        Perform perform = performRepository.findById(mt20id)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));

        Profile profile = profileRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다."));

        Optional<Wish> existingWish = wishRepository.findByPerformAndProfile(perform, profile);
        boolean wished;

        if(existingWish.isPresent()) {
            wishRepository.delete(existingWish.get());
            wished = false;
        } else {
            Wish wish = new Wish(perform, profile);
            wishRepository.save(wish);
            wished = true;
        }

        return WishRes.builder()
                .wished(wished)
                .build();
    }

}
