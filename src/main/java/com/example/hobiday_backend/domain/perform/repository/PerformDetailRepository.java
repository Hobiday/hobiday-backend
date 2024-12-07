package com.example.hobiday_backend.domain.perform.repository;

import com.example.hobiday_backend.domain.perform.entity.PerformDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerformDetailRepository extends JpaRepository<PerformDetail, Long> {
    Optional<PerformDetail> findByMt20id(String mt20id);
}