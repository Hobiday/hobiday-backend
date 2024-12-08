package com.example.hobiday_backend.domain.wish.repository;

import com.example.hobiday_backend.domain.perform.entity.Perform;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Optional<Wish> findByPerformAndProfile(Perform perform, Profile profile);
}
