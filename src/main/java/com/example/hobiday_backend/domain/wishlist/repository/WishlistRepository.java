package com.example.hobiday_backend.domain.wishlist.repository;

import com.example.hobiday_backend.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    //findbymodifiedTime 위시 누른 시각 내림차순
}
