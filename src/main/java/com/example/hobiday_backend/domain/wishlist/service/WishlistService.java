package com.example.hobiday_backend.domain.wishlist.service;

import com.example.hobiday_backend.domain.perform.entity.Perform;
import com.example.hobiday_backend.domain.perform.exception.PerformErrorCode;
import com.example.hobiday_backend.domain.perform.exception.PerformException;
import com.example.hobiday_backend.domain.perform.repository.PerformRepository;
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

    // 위시리스트 모두 조회
    public List<WishResponse> getWishlistAll(Long profileId, String rowStart, String rowEnd) {
        int start = Integer.parseInt(rowStart);
        int end = Integer.parseInt(rowEnd);
        List<Wishlist> wishlists = wishlistRepository.findWishListAll(profileId, end - start + 1, start);
        return wishlists.stream()
                .map(WishResponse::new)
                .toList();
    }

    // 위시리스트 장르별 조회
    public List<WishResponse> getWishlistByGenre(Long profileId, String genre) {
        List<Wishlist> wishlists = wishlistRepository.findWishListByGenre(profileId, genre);
        return wishlists.stream()
                .map(WishResponse::new)
                .toList();
    }

    // 위시 등록
    @Transactional
    public WishMessageResponse register(Long profileId, String performId) {
        Perform perform = performRepository.findByMt20id(performId).
                orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        String genre = perform.getGenrenm();
        if (wishlistRepository.findWishListByProfileIdAndMt20id(profileId, performId).isPresent()){
            throw new WishlistException(WishlistErrorCode.WISH_CONFLICT);
        }
        wishlistRepository.save(Wishlist.builder()
                .profileId(profileId)
                .mt20id(performId)
                .genre(genre)
                .performName(perform.getPrfnm())
                .performState(perform.getPrfstate())
                .placeName(perform.getFcltynm())
                .area(perform.getArea())
                .poster(perform.getPoster())
                .build());
        perform.wishUp();
        return new WishMessageResponse("위시 등록 성공", perform.getPrfnm());
    }

    // 위시 해제
    @Transactional
    public WishMessageResponse clear(Long profileId, String performId) {
        Perform perform = performRepository.findByMt20id(performId).
                orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        Wishlist wishlist = wishlistRepository.findWishListByProfileIdAndMt20id(profileId, performId)
                        .orElseThrow(() -> new WishlistException(WishlistErrorCode.WISH_NOT_FOUND));
        wishlistRepository.delete(wishlist);
        perform.wishDown();
        return new WishMessageResponse("위시 해제 성공", perform.getPrfnm());
    }
}
