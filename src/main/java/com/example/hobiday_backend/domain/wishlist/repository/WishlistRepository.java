package com.example.hobiday_backend.domain.wishlist.repository;

import com.example.hobiday_backend.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findWishListByProfileIdAndMt20id(Long profileId, String performId);

    @Query("select w " +
            "from Wishlist w " +
            "where w.profileId = :profileId " +
            "order by w.modifiedTime desc " +
            "limit :limit offset :offset")
    Optional<List<Wishlist>> findWishListAll(Long profileId, int limit, int offset);

    @Query("select w " +
            "from Wishlist w " +
            "where w.profileId = :profileId and w.genre = :genre " +
            "order by w.modifiedTime desc " +
            "limit :limit offset :offset")
    Optional<List<Wishlist>> findWishListByGenre(Long profileId, int limit, int offset, String genre);
}
