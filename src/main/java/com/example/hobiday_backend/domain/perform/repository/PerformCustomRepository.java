package com.example.hobiday_backend.domain.perform.repository;

import com.example.hobiday_backend.domain.perform.entity.Perform;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformCustomRepository {
    List<Perform> findAllBySelectAreaAndGenre(String keyword, List<String> genres, List<String> areas);
    List<Perform> findTenBySelectGenre(List<String> genres);
}
