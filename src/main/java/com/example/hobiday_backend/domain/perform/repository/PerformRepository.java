package com.example.hobiday_backend.domain.perform.repository;

import com.example.hobiday_backend.domain.perform.entity.Perform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PerformRepository extends JpaRepository<Perform, Long> {
    Optional<Perform> findByMt20id(String mt20id);

    // 장르별 공연 선택, 공연완료 제외
    @Query("select p " +
            "from Perform p " +
//            "where p.genrenm = :genre " +
            "where p.genrenm like concat('%', :genre, '%') and p.prfstate!='공연완료'" +
            "order by p.prfpdfrom asc " +
            "limit :limit offset :offset") // limit 개수, offset 시작값
    Optional<List<Perform>> findByGenreNm(String genre, int limit, int offset);
//            "where p.genrenm like concat('%', :genre, '%')") //+

    // 검색어 포함하는 제목의 공연 선택
    @Query("select p " +
            "from Perform p " +
            "where p.prfnm like concat('%', :search, '%') " +
            "order by p.prfpdfrom asc limit 10")
    Optional<List<Perform>> findByPrfNm(String search);

    // 모든 장르 선택 (미완성)
    @Query("select p " +
            "from Perform p " +
            "where p.prfstate!='공연완료' " +
            "order by p.prfpdfrom asc " +
            "limit :limit offset :offset")
    Optional<List<Perform>> findAllBySelect(int limit, int offset);

    // 랜덤 공연 6개 선택
//    @Query("select p " +
//            "from Perform p " +
//            "order by Rand() limit 6")
//    Optional<List<Perform>> findAllByRand();

    // (추천 검색어) 장르별 1개, 공연중 선택
    @Query("select p " +
            "from Perform p " +
            "where p.genrenm = :genre and p.prfstate!='공연완료'" +
            "order by p.prfpdfrom asc " +
            "limit 1 offset :offset") // limit 개수, offset 시작값
    Optional<List<Perform>> findBySelectGenre(String genre, int offset);
}