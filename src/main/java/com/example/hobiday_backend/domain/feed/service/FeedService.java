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
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.exception.ProfileErrorCode;
import com.example.hobiday_backend.domain.profile.exception.ProfileException;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedService {
    private final FeedRepository feedRepository;
    private final ProfileRepository profileRepository;
    private final FeedFileRepository feedFileRepository;
    private final HashTagRepository hashTagRepository;

    //피드 작성
    public FeedRes createFeed(FeedReq feedReq, Long userId) {
        Profile profile = profileRepository.findByMemberId(userId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

        // 2. Feed 생성
        Feed feed = Feed.builder()
                .content(feedReq.getContent())
                .topic(feedReq.getTopic())
                .profile(profile)
                .build();

        Feed savedFeed = feedRepository.save(feed);

        // 4. FeedFiles 생성 및 저장
        if (feedReq.getFileUrls() == null || feedReq.getFileUrls().isEmpty()) {
            // 파일 url이 없으면 오류 발생
            throw new FileUrlException(FileUrlErrorCode.EMPTY_FILE_URL);
        }

        List<FeedFile> feedFiles = feedReq.getFileUrls().stream()
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

        // 6. FeedRes 반환
        return FeedRes.builder()
                .contents(savedFeed.getContent())
                .profileName(savedFeed.getProfile().getProfileNickname()) // Profile 엔티티에 이름 필드가 있다고 가정
                .hashTag(savedFeed.getHashTags().stream()
                        .map(HashTag::getHashTag)
                        .toList()) // 저장된 HashTag 리스트
                // url도 반환해야 함
                .feedFiles(savedFeed.getFeedFiles().stream()
                        .map(FeedFile::getFileUrl)
                        .toList())
                .likeCount(savedFeed.getLikeCount())
                .isLiked(false) // 기본값 설정
                .build();
    }

    //피드 수정
    public FeedRes updateFeed(FeedReq feedReq, Long memberId,Long feedId) {
        // 멤버 id로 프로필 찾기
        Profile profile = profileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));
        // 이 멤버가 쓴 피드가 맞는지 확인하기

        // 2. 피드 조회 및 작성자 확인
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        if (!feed.getProfile().getId().equals(profile.getId())) {
            throw new FeedException(FeedErrorCode.FEED_UPDATE_ACCESS_DENIED);
        }

        // 3. 피드 수정
        feed.update(feedReq.getContent(), feedReq.getTopic(), feedReq.getFileUrls(), feedReq.getHashTags());

        // 4. 응답 반환
        return FeedRes.builder()
                .contents(feed.getContent())
                .profileName(feed.getProfile().getProfileNickname())
                .hashTag(feed.getHashTags().stream()
                        .map(HashTag::getHashTag)
                        .toList())
                .feedFiles(feed.getFeedFiles().stream()
                        .map(FeedFile::getFileUrl)
                        .toList())
                .likeCount(feed.getLikeCount())
                .isLiked(false)
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

}
