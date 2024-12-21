package com.example.hobiday_backend.domain.perform.service;

import com.example.hobiday_backend.domain.feed.entity.Feed;
import com.example.hobiday_backend.domain.feed.repository.FeedRepository;
import com.example.hobiday_backend.domain.perform.dto.response.*;
import com.example.hobiday_backend.domain.perform.entity.FacilityDetail;
import com.example.hobiday_backend.domain.perform.entity.Perform;
import com.example.hobiday_backend.domain.perform.entity.PerformDetail;
import com.example.hobiday_backend.domain.perform.exception.PerformErrorCode;
import com.example.hobiday_backend.domain.perform.exception.PerformException;
import com.example.hobiday_backend.domain.perform.repository.FacilityRepository;
import com.example.hobiday_backend.domain.perform.repository.PerformCustomRepositoryImpl;
import com.example.hobiday_backend.domain.perform.repository.PerformDetailRepository;
import com.example.hobiday_backend.domain.perform.repository.PerformRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.hobiday_backend.domain.feed.entity.QFeed.feed;


@Slf4j
@RequiredArgsConstructor
@Service
public class PerformService {
    private final PerformRepository performRepository;
    private final PerformDetailRepository performDetailRepository;
    private final FacilityRepository facilityRepository;
    private final PerformCustomRepositoryImpl performCustomRepositoryImpl;
    private final FeedRepository feedRepository;


    // 모든 장르 조회: 공연 시작순
    public List<PerformResponse> getPerformsAll(String rowStart, String rowEnd){
        int start = Integer.parseInt(rowStart);
        int end = Integer.parseInt(rowEnd);
        List<Perform> performList = performRepository.findAllBySelect(end - start + 1, start)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        return performList.stream()
                .map(PerformResponse::new)
                .toList();
    }

    // 모든 장르 조회: 공연종료 임박순
    public List<PerformResponse> getPerformsAllDeadline(String rowStart, String rowEnd){
        int start = Integer.parseInt(rowStart);
        int end = Integer.parseInt(rowEnd);
        List<Perform> performList = performRepository.findAllBySelectDeadline(end - start + 1, start)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        return performList.stream()
                .map(PerformResponse::new)
                .toList();
    }

    // 장르별 공연 리스트 조회
    public List<PerformResponse> getPerformListByGenre(String genre, String rowStart, String rowEnd) {
        int start = Integer.parseInt(rowStart);
        int end = Integer.parseInt(rowEnd);
//       int limit = rowEnd - rowStart + 1;
//       int offset = rowStart;
//       log.info("들어와서: " + genre);
        List<Perform> performList = performRepository.findByGenreNm(genre, end - start + 1, start)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        return performList.stream()
                .map(PerformResponse::new)
                .toList();
    }

    // 공연기본 조회
    public PerformResponse getPerform(String mt20id) {
        Perform perform = performRepository.findByMt20id(mt20id)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        return PerformResponse.builder()
                .performId(perform.getMt20id())
                .performName(perform.getPrfnm())
                .startDate(perform.getPrfpdfrom())
                .endDate(perform.getPrfpdto())
                .genreName(perform.getGenrenm())
                .performState(perform.getPrfstate())
                .placeName(perform.getFcltynm())
                .openRun(perform.getOpenrun())
                .area(perform.getArea())
                .poster(perform.getPoster())
                .wishCount(perform.getWishCount())
                .build();
    }

    // 공연상세 조회
    public PerformDetailResponse getPerformDetailResponse(String mt20id) {
        PerformDetail performDetail = performDetailRepository.findByMt20id(mt20id)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        Perform perform = performRepository.findByMt20id(mt20id)
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
                .feedCount(feedRepository.countByPerform(perform))
                .build();
    }

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

    // 공연 검색(이름) 결과 목록
    public List<PerformResponse> getPerformsBySearch(String search) {
        List<Perform> performList = performRepository.findByPrfNm(search)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        return performList.stream()
                .map(PerformResponse::new)
                .toList();
    }

    public PerformAllResponse getPerformAll(String mt20id) {
        Perform perform = performRepository.findByMt20id(mt20id)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));

        PerformDetail performDetail = performDetailRepository.findByMt20id(mt20id)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));

        return PerformAllResponse.builder()
                .performId(perform.getMt20id())
                .performName(perform.getPrfnm())
                .startDate(perform.getPrfpdfrom())
                .endDate(perform.getPrfpdto())
                .genreName(perform.getGenrenm())
                .performState(perform.getPrfstate())
                .placeName(perform.getFcltynm())
                .openRun(perform.getOpenrun())
                .area(perform.getArea())
                .poster(perform.getPoster())
                .wishCount(perform.getWishCount())
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
                .feedCount(feedRepository.countByPerform(perform))
                .build();
    }

    // 공연 추천 검색어 목록
    public List<PerformRecommendListResponse> getPerformsByRecommends(List<String> profileGenreList) {

        // 선택한 장르를 한번씩 돌아가며 리스트 10개 채워놓음
//        int i = 0;
//        int iniSize = profileGenreList.size();
//        while (profileGenreList.size() != 10){
//            profileGenreList.add(profileGenreList.get(i++));
//            if (i==iniSize) i = 0;
//        }
//
////        log.info("프로필장르리스트: " + profileGenreList);
//
//        // 응답할 공연 10개 꺼내오기
//        int cnt = 0;
//        List<Perform> performList = new ArrayList<>();
//        List<Perform> perform;
//        while (cnt!=10){
//            perform = performRepository.findBySelectGenre(profileGenreList.get(cnt), cnt)
//                    .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
//            performList.addAll(perform);
//            cnt++;
//        }
        List<Perform> performList = performCustomRepositoryImpl.findTenBySelectGenre(profileGenreList);

        return performList.stream()
                .map(PerformRecommendListResponse::new)
                .toList();
    }

    // 키워드, 지역들, 장르들 검색
    public List<PerformResponse> getPerformsBySearchDetails(String keyword, List<String> genres, List<String> areas) {
        List<Perform> performList = performCustomRepositoryImpl.findAllBySelectAreaAndGenre(keyword, genres, areas);
//        for(Perform perform : performList){
//            log.info("공연 정보: {} | {} | {}", perform.getPrfnm(), perform.getGenrenm(), perform.getArea());
//        }

        return performList.stream()
                .map(PerformResponse::new)
                .toList();
    }

    public List<FeedsByPerformResponse> getFeedsByPerformId(String performId) {
        Perform perform = performRepository.findByMt20id(performId)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        List<Feed> feedList = feedRepository.findAllByPerformOrderByCreatedTimeDesc(perform);

        return feedList.stream()
                .map(FeedsByPerformResponse::from)
                .toList();
    }

//    public List<Perform> findAllBySelectAreaAndGenre(final String genre, final String area) {
//        return queryFactory
//                .selectfrom(Perform)
//    }


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

}
