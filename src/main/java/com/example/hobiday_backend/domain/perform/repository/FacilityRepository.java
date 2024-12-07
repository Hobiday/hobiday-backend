package com.example.hobiday_backend.domain.perform.repository;

import com.example.hobiday_backend.domain.perform.entity.FacilityDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityRepository extends JpaRepository<FacilityDetail, Long> {
    Optional<FacilityDetail> findByMt10id(String mt10id) ;
}