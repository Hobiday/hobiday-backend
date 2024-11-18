package com.example.hobiday_backend.domain.performance.repository;

import com.example.hobiday_backend.domain.performance.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
//    Optional<PerformanceRepository> findById(Long id);
}