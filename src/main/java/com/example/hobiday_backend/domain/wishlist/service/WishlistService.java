package com.example.hobiday_backend.domain.wishlist.service;

import com.example.hobiday_backend.domain.perform.dto.response.PerformResponse;
import com.example.hobiday_backend.domain.perform.entity.Perform;
import com.example.hobiday_backend.domain.perform.exception.PerformErrorCode;
import com.example.hobiday_backend.domain.perform.exception.PerformException;
import com.example.hobiday_backend.domain.perform.repository.PerformRepository;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.wishlist.dto.WishMessageResponse;
import com.example.hobiday_backend.domain.wishlist.dto.WishResponse;
import com.example.hobiday_backend.domain.wishlist.entity.Wishlist;
import com.example.hobiday_backend.domain.wishlist.exception.WishlistErrorCode;
import com.example.hobiday_backend.domain.wishlist.exception.WishlistException;
import com.example.hobiday_backend.domain.wishlist.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WishlistService {
    private final PerformRepository performRepository;
    private final WishlistRepository wishlistRepository;

    // 위시 등록
    public WishMessageResponse register(Long profileId, String performId) {
        Perform perform = performRepository.findByMt20id(performId).
                orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        String genre = perform.getGenrenm();
        wishlistRepository.save(Wishlist.builder()
                .profile_id(profileId)
                .mt20id(performId)
                .genre(genre)
                .build());
        perform.likeUp();
        return new WishMessageResponse("위시 등록 성공");
    }

    // 위시 해제
    @Transactional
    public WishMessageResponse clear(Long profileId, String performId) {
        Perform perform = performRepository.findByMt20id(performId).
                orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        Wishlist wishlist = wishlistRepository.findWishListByProfileIdAndMt20id(profileId, performId)
                        .orElseThrow(() -> new WishlistException(WishlistErrorCode.WISH_DELETE_ACCESS_DENIED));
        wishlistRepository.delete(wishlist);
        perform.likeDown();
        return new WishMessageResponse("위시 해제 성공");
    }

    // 위시리스트 모두 조회
    public List<WishResponse> getWishlistAll(Long profileId, String rowStart, String rowEnd) {
        int start = Integer.parseInt(rowStart);
        int end = Integer.parseInt(rowEnd);
        List<Wishlist> wishlists = wishlistRepository.findWishListAll(profileId, end - start + 1, start)
                .orElseThrow(() -> new WishlistException(WishlistErrorCode.WISH_NOT_FOUND));
        return wishlists.stream()
                .map(WishResponse::new)
                .toList();
    }

    // 위시리스트 장르별 조회
    public List<WishResponse> getWishlistByGenre(Long profileId, String rowStart, String rowEnd, String genre) {
        int start = Integer.parseInt(rowStart);
        int end = Integer.parseInt(rowEnd);
        List<Wishlist> wishlists = wishlistRepository.findWishListByGenre(profileId, end - start + 1, start, genre)
                .orElseThrow(() -> new WishlistException(WishlistErrorCode.WISH_NOT_FOUND));
        return wishlists.stream()
                .map(WishResponse::new)
                .toList();
    }
}
