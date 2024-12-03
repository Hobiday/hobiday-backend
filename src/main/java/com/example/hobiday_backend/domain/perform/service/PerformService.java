package com.example.hobiday_backend.domain.perform.service;

import com.example.hobiday_backend.domain.perform.dto.response.FacilityResponse;
import com.example.hobiday_backend.domain.perform.dto.response.PerformDetailResponse;
import com.example.hobiday_backend.domain.perform.dto.response.PerformResponse;
import com.example.hobiday_backend.domain.perform.entity.FacilityDetail;
import com.example.hobiday_backend.domain.perform.entity.Perform;
import com.example.hobiday_backend.domain.perform.entity.PerformDetail;
import com.example.hobiday_backend.domain.perform.exception.PerformErrorCode;
import com.example.hobiday_backend.domain.perform.exception.PerformException;
import com.example.hobiday_backend.domain.perform.repository.FacilityRepository;
import com.example.hobiday_backend.domain.perform.repository.PerformDetailRepository;
import com.example.hobiday_backend.domain.perform.repository.PerformRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.hobiday_backend.domain.perform.util.KopisParsing.GENRE_CODES_REQUEST;

@Slf4j
@RequiredArgsConstructor
@Service
public class PerformService {
    private final PerformRepository performRepository;
    private final PerformDetailRepository performDetailRepository;
    private final FacilityRepository facilityRepository;

    // 시설상세 조회
    public FacilityResponse getFacilityResponse(String mt10id) {
        FacilityDetail facility = facilityRepository.findByMt10id(mt10id)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));

        return FacilityResponse.builder()
                .mt10id(facility.getMt10id())
                .fcltynm(facility.getFcltynm())
                .telno(facility.getTelno())
                .adres(facility.getAdres())
                .la(facility.getLa())
                .lo(facility.getLo())
                .cafe(facility.getCafe())
                .parkinglot(facility.getParkinglot())
                .build();
    }

    // 공연상세 조회
    public PerformDetailResponse getPerformDetailResponse(String mt20id) {
        PerformDetail performDetail = performDetailRepository.findByMt20id(mt20id)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));

        return PerformDetailResponse.builder()
                .mt20id(performDetail.getMt20id())
                .mt10id(performDetail.getMt10id())
                .prfcast(performDetail.getPrfcast())
                .prfruntime(performDetail.getPrfruntime())
                .prfage(performDetail.getPrfage())
                .pcseguidance(performDetail.getPcseguidance())
                .styurl(performDetail.getStyurl())
                .dtguidance(performDetail.getDtguidance())
                .relatenm(performDetail.getRelatenm())
                .relateurl(performDetail.getRelateurl())
                .build();
    }

    // 공연기본 조회
    public PerformResponse getPerform(String mt20id) {
        Perform perform = performRepository.findByMt20id(mt20id)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        return PerformResponse.builder()
                .mt20id(perform.getMt20id())
                .prfnm(perform.getPrfnm())
                .prfpdfrom(perform.getPrfpdfrom())
                .genrenm(perform.getGenrenm())
                .prfstate(perform.getPrfstate())
                .fcltynm(perform.getFcltynm())
                .openrun(perform.getOpenrun())
                .area(perform.getArea())
                .poster(perform.getPoster())
                .likeCount(perform.getLikeCount())
                .build();
    }

    // 장르별 공연 리스트 조회
    public List<PerformResponse> getPerformListByGenre(String genre) {
        log.info("들어와서: " + genre);
       List<Perform> performList = performRepository.findByGenreNm(genre)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        return performList.stream()
                .map(PerformResponse::new)
                .toList();
    }

    // 공연 검색(이름) 결과 목록
    public List<PerformResponse> getPerformsBySearch(String search) {
        List<Perform> performList = performRepository.findByPrfNm(search)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        return performList.stream()
                .map(PerformResponse::new)
                .toList();
    }

    // 모든 장르로 조회
    public List<PerformResponse> getPerformsAll() {
        // 장르별 5개씩 가져와서
        // 우선 집합으로 랜덤
        List<Perform> performList = new ArrayList<>();
//        List<Perform> performGenreList;
        for(String genre : GENRE_CODES_REQUEST.keySet()){
            log.info("genre: " + genre);
            assert false;
            performList.addAll(performRepository.findAllByGenreNm(genre)
                    .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND)));
        }
        assert false;

//        HashSet<Perform> performSet = new HashSet<>(List.of(performList.toArray(new Perform[0])));
        Set<Perform> performSet = Set.copyOf(performList);
        List<Perform> performListFinal = new ArrayList<>(performSet);
        return performListFinal.stream()
                .map(PerformResponse::new)
                .toList();
    }

    // 홈화면 추천 공연 6개 | 현재 랜덤
    public List<PerformResponse> getMainPerforms(/*List<String> profileGenreList*/) {
        List<Perform> performList = performRepository.findAllByRand().
            orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        return performList.stream()
                .map(PerformResponse::new)
                .toList();
    }
}
