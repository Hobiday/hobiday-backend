package com.example.hobiday_backend.domain.perform.service;

import com.example.hobiday_backend.domain.perform.dto.reqeust.PerformAllRequest;
import com.example.hobiday_backend.domain.perform.dto.reqeust.PerformGenreRequest;
import com.example.hobiday_backend.domain.perform.dto.response.FacilityResponse;
import com.example.hobiday_backend.domain.perform.dto.response.PerformDetailResponse;
import com.example.hobiday_backend.domain.perform.dto.response.PerformRecommendListResponse;
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
                .facilityId(facility.getMt10id())
                .facilityName(facility.getFcltynm())
                .telephone(facility.getTelno())
                .address(facility.getAdres())
                .latitude(facility.getLa())
                .longitude(facility.getLo())
                .cafe(facility.getCafe())
                .parkingLot(facility.getParkinglot())
                .build();
    }

    // 공연상세 조회
    public PerformDetailResponse getPerformDetailResponse(String mt20id) {
        PerformDetail performDetail = performDetailRepository.findByMt20id(mt20id)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));

        return PerformDetailResponse.builder()
                .performId(performDetail.getMt20id())
                .facilityId(performDetail.getMt10id())
                .cast(performDetail.getPrfcast())
                .runtime(performDetail.getPrfruntime())
                .perform(performDetail.getPrfage())
                .ticketPrice(performDetail.getPcseguidance())
                .storyUrl(performDetail.getStyurl())
                .showtime(performDetail.getDtguidance())
                .reservationChannel(performDetail.getRelatenm())
                .reservationUrl(performDetail.getRelateurl())
                .build();
    }

    // 공연기본 조회
    public PerformResponse getPerform(String mt20id) {
        Perform perform = performRepository.findByMt20id(mt20id)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        return PerformResponse.builder()
                .performId(perform.getMt20id())
                .performName(perform.getPrfnm())
                .startDate(perform.getPrfpdfrom())
                .endDate(perform.getGenrenm())
                .genreName(perform.getPrfstate())
                .performState(perform.getFcltynm())
                .openRun(perform.getOpenrun())
                .area(perform.getArea())
                .poster(perform.getPoster())
                .wishCount(perform.getWishCount())
                .build();
    }

    // 장르별 공연 리스트 조회
    public List<PerformResponse> getPerformListByGenre(PerformGenreRequest performGenreRequest) {
        String genre = performGenreRequest.genre;
        int rowStart = Integer.parseInt(performGenreRequest.rowStart);
        int rowEnd = Integer.parseInt(performGenreRequest.rowEnd);
//       int limit = rowEnd - rowStart + 1;
//       int offset = rowStart;
//       log.info("들어와서: " + genre);
        List<Perform> performList = performRepository.findByGenreNm(genre, rowEnd - rowStart + 1, rowStart)
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
//    public List<PerformResponse> getPerformsAll() {
//        // 장르별 5개씩 가져와서
//        // 우선 집합으로 랜덤
//        List<Perform> performList = new ArrayList<>();
////        List<Perform> performGenreList;
//        for(String genre : GENRE_CODES_REQUEST.keySet()){
//            log.info("genre: " + genre);
//            assert false;
//            performList.addAll(performRepository.findAllByGenreNm(genre)
//                    .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND)));
//        }
//        assert false;
//
////        HashSet<Perform> performSet = new HashSet<>(List.of(performList.toArray(new Perform[0])));
//        Set<Perform> performSet = Set.copyOf(performList);
//        List<Perform> performListFinal = new ArrayList<>(performSet);
//        return performListFinal.stream()
//                .map(PerformResponse::new)
//                .toList();
//    }

//    // 홈화면 추천 공연 6개 | 현재 랜덤
//    public List<PerformResponse> getMainPerforms(/*List<String> profileGenreList*/) {
//        List<Perform> performList = performRepository.findAllByRand().
//            orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
//        return performList.stream()
//                .map(PerformResponse::new)
//                .toList();
//    }

    // 모든 장르 조회
    public List<PerformResponse> getPerformsAll(List<String> profileGenreList, PerformAllRequest performAllRequest){
        // 장르별로 index 어디까지 했는지 기억하고 있어야함

//        String genre = performGenreRequest.genre;
        int rowStart = Integer.parseInt(performAllRequest.rowStart);
        int rowEnd = Integer.parseInt(performAllRequest.rowEnd);
        List<Perform> performList = performRepository.findAllBySelect(rowEnd - rowStart + 1, rowStart)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        return performList.stream()
                .map(PerformResponse::new)
                .toList();
    }


    // 공연 추천 검색어 목록
    public List<PerformRecommendListResponse> getPerformsByRecommends(List<String> profileGenreList) {

        // 선택한 장르를 한번씩 돌아가며 리스트 10개 채워놓음
        int i = 0;
        int iniSize = profileGenreList.size();
        while (profileGenreList.size() != 10){
            profileGenreList.add(profileGenreList.get(i++));
            if (i==iniSize) i = 0;
        }

//        log.info("프로필장르리스트: " + profileGenreList);

        // 응답할 공연 10개 꺼내오기
        int cnt = 0;
        List<Perform> performList = new ArrayList<>();
        List<Perform> perform;
        while (cnt!=10){
            perform = performRepository.findBySelectGenre(profileGenreList.get(cnt), cnt)
                    .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
            performList.addAll(perform);
            cnt++;
        }

        return performList.stream()
                .map(PerformRecommendListResponse::new)
                .toList();
    }
}
