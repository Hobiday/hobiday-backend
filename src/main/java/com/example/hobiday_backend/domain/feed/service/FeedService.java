package com.example.hobiday_backend.domain.feed.service;

import com.example.hobiday_backend.domain.feed.dto.FeedReq;
import com.example.hobiday_backend.domain.feed.dto.FeedRes;
import com.example.hobiday_backend.domain.feed.entity.Feed;
import com.example.hobiday_backend.domain.feed.entity.FeedFile;
import com.example.hobiday_backend.domain.feed.entity.HashTag;
import com.example.hobiday_backend.domain.feed.exception.*;
import com.example.hobiday_backend.domain.feed.repository.FeedFileRepository;
import com.example.hobiday_backend.domain.feed.repository.FeedRepository;
import com.example.hobiday_backend.domain.feed.repository.HashTagRepository;
import com.example.hobiday_backend.domain.like.repository.LikeRepository;
import com.example.hobiday_backend.domain.perform.entity.Perform;
import com.example.hobiday_backend.domain.perform.exception.PerformErrorCode;
import com.example.hobiday_backend.domain.perform.exception.PerformException;
import com.example.hobiday_backend.domain.perform.repository.PerformRepository;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.exception.ProfileErrorCode;
import com.example.hobiday_backend.domain.profile.exception.ProfileException;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedService {
    private final FeedRepository feedRepository;
    private final ProfileRepository profileRepository;
    private final FeedFileRepository feedFileRepository;
    private final HashTagRepository hashTagRepository;
    private final PerformRepository performRepository;
    private final LikeRepository likeRepository;

    //피드 작성
    public FeedRes createFeed(FeedReq feedReq, Long userId) {
        Profile profile = profileRepository.findByMemberId(userId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

        // 공연 정보가 있을 경우 처리
        Perform perform = null;
        if (feedReq.getPerformId() != null) {
            perform = performRepository.findByMt20id(feedReq.getPerformId())
                    .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        }


        // 2. Feed 생성
        Feed feed = Feed.builder()
                .content(feedReq.getContent())
                .topic(feedReq.getTopic())
                .profile(profile)
                .perform(perform)
                .build();

        Feed savedFeed = feedRepository.save(feed);

        // 4. FeedFiles 생성 및 저장
        if (feedReq.getFileUrls() == null || feedReq.getFileUrls().isEmpty()) {
            // 파일 url이 없으면 오류 발생
            throw new FileUrlException(FileUrlErrorCode.EMPTY_FILE_URL);
        }

        // 4-2. cloudfront url로 이미지 저장
        List<String> cdnFileUrls = new ArrayList<>();
        for(String s3url : feedReq.getFileUrls()) {
            String[] urls = s3url.split("/");
            if (urls.length < 4){
                throw new FileUrlException(FileUrlErrorCode.WRONG_FILE_URL);
            }
            urls[2] = "cdn.hobiday.site";
            String cdnUrl = "https://";
            for (int i = 2; i < urls.length; i++) {
                cdnUrl += urls[i] + "/";
            }
            cdnFileUrls.add(cdnUrl.substring(0, cdnUrl.length() - 1));
        }

//        List<FeedFile> feedFiles = feedReq.getFileUrls().stream()
        List<FeedFile> feedFiles = cdnFileUrls.stream()
                .map(fileUrl -> FeedFile.builder()
                        .fileUrl(fileUrl)
                        .feed(savedFeed) // 부모 설정
                        .build())
                .toList();

        feedFileRepository.saveAll(feedFiles); // db에 저장
        savedFeed.getFeedFiles().addAll(feedFiles); // Feed의 리스트에 추가

        // 5. HashTags 생성 및 저장
        if (feedReq.getHashTags() == null || feedReq.getHashTags().isEmpty()) {
            // 해시태그가 비어있으면 발상해는 에러 코드
            throw new HashTagException(HashTagErrorCode.EMPTY_HASH_TAG);
        }

        List<HashTag> hashTags = feedReq.getHashTags().stream()
                .map(tagName -> HashTag.builder()
                        .hashTag(tagName)
                        .feed(savedFeed)
                        .build())
                .toList();

        hashTagRepository.saveAll(hashTags);
        savedFeed.getHashTags().addAll(hashTags);

        // 6. FeedRes 반환 (상대 시간 추가)
        return FeedRes.builder()
                .feedId(savedFeed.getId())
                .contents(savedFeed.getContent())
                .profileName(savedFeed.getProfile().getProfileNickname())
                .profileId(savedFeed.getProfile().getId())
                .profileImageUrl(savedFeed.getProfile().getProfileImageUrl())
                .hashTag(savedFeed.getHashTags().stream()
                        .map(HashTag::getHashTag)
                        .toList())
                .feedFiles(savedFeed.getFeedFiles().stream()
                        .map(FeedFile::getFileUrl)
                        .toList())
                .likeCount(savedFeed.getLikeCount())
                .commentCount(savedFeed.getCommentList().size())
                .isLiked(false)
                .relativeTime(getRelativeTime(savedFeed.getCreatedTime())) // 상대 시간 추가
                .performName(perform.getPrfnm())
                .startDate(perform.getPrfpdfrom())
                .endDate(perform.getPrfpdto())
                .genreName(perform.getGenrenm())
                .performState(perform.getPrfstate())
                .placeName(perform.getFcltynm())
                .openRun(perform.getOpenrun())
                .area(perform.getArea())
                .poster(perform.getPoster())
                .performLikeCount(perform.getWishCount())
                .build();
    }

    //피드 수정
    public FeedRes updateFeed(FeedReq feedReq, Long memberId,Long feedId) {
        // 멤버 id로 프로필 찾기
        Profile profile = profileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));
        //
        Perform perform = performRepository.findByMt20id(feedReq.getPerformId())
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));
        // 2. 피드 조회 및 작성자 확인
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        if (!feed.getProfile().getId().equals(profile.getId())) {
            throw new FeedException(FeedErrorCode.FEED_UPDATE_ACCESS_DENIED);
        }

        // 3. 피드 수정
        feed.update(feedReq.getContent(), feedReq.getTopic(), feedReq.getFileUrls(), feedReq.getHashTags());

        // FeedRes 반환 (상대 시간 추가)
        return FeedRes.builder()
                .feedId(feed.getId())
                .contents(feed.getContent())
                .profileName(feed.getProfile().getProfileNickname())
                .profileId(feed.getProfile().getId())
                .profileImageUrl(feed.getProfile().getProfileImageUrl())
                .hashTag(feed.getHashTags().stream()
                        .map(HashTag::getHashTag)
                        .toList())
                .feedFiles(feed.getFeedFiles().stream()
                        .map(FeedFile::getFileUrl)
                        .toList())
                .likeCount(feed.getLikeCount())
                .commentCount(feed.getCommentList().size())
                .isLiked(false)
                .relativeTime(getRelativeTime(feed.getCreatedTime())) // 상대 시간 추가
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
                .performLikeCount(perform.getWishCount())
                .build();
    }

    public void deleteFeed(Long memberId, Long feedId) {
        // 1. 피드 조회
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        // 2. 작성자 확인
        if (!feed.getProfile().getMember().getId().equals(memberId)) {
            throw new FeedException(FeedErrorCode.FEED_DELETE_ACCESS_DENIED);
        }

        // 3. 피드 삭제 (연관 데이터 포함)
        feedRepository.delete(feed);
    }

    // 최신순 조회
    @Transactional(readOnly = true)
    public List<FeedRes> getFeedsByLatest(Long userId) {
        List<Feed> feeds = feedRepository.findAllByOrderByCreatedTimeDesc();
//        if (feeds.isEmpty()) {
//            throw new FeedException(FeedErrorCode.FEED_LIST_EMPTY); // 피드가 없을 경우 예외 발생
//        }
        // 초기화면에는 피드가 없을 수도 있기 때문에
        Profile profile = profileRepository.findByMemberId(userId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));
        return convertToFeedResList(feeds, profile.getId());
    }

    // 좋아요 순 조회
    @Transactional(readOnly = true)
    public List<FeedRes> getFeedsByLikes(Long userId) {
        List<Feed> feeds = feedRepository.findAllByOrderByLikeCountDesc();
//        if (feeds.isEmpty()) {
//            throw new FeedException(FeedErrorCode.FEED_LIST_EMPTY); // 피드가 없을 경우 예외 발생
//        }
        Profile profile = profileRepository.findByMemberId(userId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));
        return convertToFeedResList(feeds, profile.getId());
    }

    // 단일 피드 조회
    @Transactional(readOnly = true)
    public FeedRes getFeedById(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));
        Profile findProfile=feed.getProfile();
        Profile profile=profileRepository.findById(findProfile.getId())
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));
        // 피드 작성 후 저장된 피드 공연 정보를 가져오기
        Perform findPerform=feed.getPerform();
        if (findPerform == null) {
            throw new PerformException(PerformErrorCode.PERFORM_NOT_FOUND);
        }
        String performId = findPerform.getMt20id();

        Perform perform = performRepository.findByMt20id(performId)
                .orElseThrow(() -> new PerformException(PerformErrorCode.PERFORM_NOT_FOUND));

        return FeedRes.builder()
                .feedId(feed.getId())
                .contents(feed.getContent())
                .profileName(feed.getProfile().getProfileNickname())
                .profileId(feed.getProfile().getId())
                .profileImageUrl(feed.getProfile().getProfileImageUrl())
                .hashTag(feed.getHashTags().stream()
                        .map(HashTag::getHashTag)
                        .toList())
                .feedFiles(feed.getFeedFiles().stream()
                        .map(FeedFile::getFileUrl)
                        .toList())
                .likeCount(feed.getLikeCount())
                .commentCount(feed.getCommentList().size())
                .isLiked(likeRepository.existsByFeedAndProfile(feed,profile))
                .relativeTime(getRelativeTime(feed.getCreatedTime()))
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
                .performLikeCount(perform.getWishCount())
                .build();
    }

    // 프로필 하위 전체 피드 조회
    @Transactional(readOnly = true)
    public List<FeedRes> getProfileFeeds(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));
        Long findProfileId = profile.getId();
        List<Feed> feeds = feedRepository.findAllByProfileIdOrderByCreatedTimeDesc(profileId);
        return convertToFeedResList(feeds,findProfileId);
    }

    // Feed 엔티티를 FeedRes DTO로 변환
    private List<FeedRes> convertToFeedResList(List<Feed> feeds, Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

        return feeds.stream()
                .map(feed -> FeedRes.builder()
                        .feedId(feed.getId())
                        .contents(feed.getContent())
                        .profileName(feed.getProfile().getProfileNickname())
                        .profileId(feed.getProfile().getId())
                        .profileImageUrl(feed.getProfile().getProfileImageUrl())
                        .hashTag(feed.getHashTags().stream()
                                .map(HashTag::getHashTag)
                                .collect(Collectors.toList()))
                        .feedFiles(feed.getFeedFiles().stream()
                                .map(FeedFile::getFileUrl)
                                .collect(Collectors.toList()))
                        .likeCount(feed.getLikeCount())
                        .commentCount(feed.getCommentList().size())
                        .isLiked(likeRepository.existsByFeedAndProfile(feed,profile))
                        .relativeTime(getRelativeTime(feed.getCreatedTime())) // 상대 시간 추가
                        // 공연 정보 추가
                        .performId(feed.getPerform() != null ? feed.getPerform().getMt20id() : null)
                        .performName(feed.getPerform() != null ? feed.getPerform().getPrfnm() : null)
                        .startDate(feed.getPerform() != null ? feed.getPerform().getPrfpdfrom() : null)
                        .endDate(feed.getPerform() != null ? feed.getPerform().getPrfpdto() : null)
                        .genreName(feed.getPerform() != null ? feed.getPerform().getGenrenm() : null)
                        .performState(feed.getPerform() != null ? feed.getPerform().getPrfstate() : null)
                        .placeName(feed.getPerform() != null ? feed.getPerform().getFcltynm() : null)
                        .openRun(feed.getPerform() != null ? feed.getPerform().getOpenrun() : null)
                        .area(feed.getPerform() != null ? feed.getPerform().getArea() : null)
                        .poster(feed.getPerform() != null ? feed.getPerform().getPoster() : null)
                        .performLikeCount(feed.getPerform() != null ? feed.getPerform().getWishCount() : null)
                        .build())
                .collect(Collectors.toList());
    }

    // 상대 시간 계산
    private String getRelativeTime(LocalDateTime createdTime) {
        Duration duration = Duration.between(createdTime, LocalDateTime.now());
        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) {
            return seconds + "초 전";
        } else if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else {
            return days + "일 전";
        }
    }
}
