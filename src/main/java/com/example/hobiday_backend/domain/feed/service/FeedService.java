package com.example.hobiday_backend.domain.feed.service;

import com.example.hobiday_backend.domain.feed.dto.FeedReq;
import com.example.hobiday_backend.domain.feed.dto.FeedRes;
import com.example.hobiday_backend.domain.feed.entity.Feed;
import com.example.hobiday_backend.domain.feed.entity.FeedFile;
import com.example.hobiday_backend.domain.feed.entity.HashTag;
import com.example.hobiday_backend.domain.feed.repository.FeedFileRepository;
import com.example.hobiday_backend.domain.feed.repository.FeedRepository;
import com.example.hobiday_backend.domain.feed.repository.HashTagRepository;
import com.example.hobiday_backend.domain.profile.entity.Profile;
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
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다"));

        // 2. Feed 생성
        Feed feed = Feed.builder()
                .content(feedReq.getContent())
                .topic(feedReq.getTopic())
                .profile(profile)
                .build();

        Feed savedFeed = feedRepository.save(feed);

        // 4. FeedFiles 생성 및 저장
        if (feedReq.getFileUrls() == null || feedReq.getFileUrls().isEmpty()) {
            throw new IllegalArgumentException("파일 URL 리스트는 비어 있을 수 없습니다.");
        }

        List<FeedFile> feedFiles = feedReq.getFileUrls().stream()
                .map(fileUrl -> FeedFile.builder()
                        .fileUrl(fileUrl)
                        .feed(savedFeed)
                        .build())
                .toList();

        feedFileRepository.saveAll(feedFiles);

        // 5. HashTags 생성 및 저장
        if (feedReq.getHashTags() == null || feedReq.getHashTags().isEmpty()) {
            throw new IllegalArgumentException("해시 태그는 비어 있을 수 없습니다");
        }

        List<HashTag> hashTags = feedReq.getHashTags().stream()
                .map(tagName -> HashTag.builder()
                        .hashTag(tagName)
                        .feed(savedFeed)
                        .build())
                .toList();

        hashTagRepository.saveAll(hashTags);

        // 6. FeedRes 반환
        return FeedRes.builder()
                .contents(savedFeed.getContent())
                .profileName(savedFeed.getProfile().getProfileNickname()) // Profile 엔티티에 이름 필드가 있다고 가정
                .hashTag(savedFeed.getHashTags()) // 저장된 HashTag 리스트
                .likeCount(savedFeed.getLikeCount())
                .isLiked(false) // 기본값 설정
                .build();
    }

    public List<FeedRes> getAllFeedsByLatest() {
        List<Feed> feeds = feedRepository.findAllByOrderByCreatedTimeDesc();

        return feeds.stream()
                .peek(Feed::updateCommentCount)
                .map(feed -> FeedRes.builder()
                        .id(feed.getId())
                        .contents(feed.getContent())
                        .profileName(feed.getProfile().getProfileNickname())
                        .profileImageUrl(feed.getProfile().getProfileImageUrl())
                        .hashTag(feed.getHashTags())
                        .likeCount(feed.getLikeCount())
                        .commentCount(feed.getCommentCount())
                        .isLiked(false)
                        .createdTime(feed.getCreatedTime())
                        .modifiedTime(feed.getModifiedTime())
                        .build())
                .toList();
    }

    public List<FeedRes> getFeedsByLatest(Long profileId) {
     //   List<Feed> feeds = feedRepository.findByMemberIdOrderByCreatedTimeDesc(memberId);
        List<Feed> feeds = feedRepository.findAllByProfileIdOrderByCreatedTimeDesc(profileId);
        return feeds.stream()
                .peek(Feed::updateCommentCount)
                .map(feed -> FeedRes.builder()
                        .id(feed.getId())
                        .contents(feed.getContent())
                        .profileName(feed.getProfile().getProfileNickname())
                        .profileImageUrl(feed.getProfile().getProfileImageUrl())
                        .commentCount(feed.getCommentCount())
                        .hashTag(feed.getHashTags())
                        .likeCount(feed.getLikeCount())
                        .isLiked(false)
                        .createdTime(feed.getCreatedTime())
                        .modifiedTime(feed.getModifiedTime())
                        .build())
                .toList();
    }

    public List<FeedRes> getFeedsByLikeCount() {
        List<Feed> feeds = feedRepository.findAllByOrderByLikeCountDesc();

        return feeds.stream()
                .peek(Feed::updateCommentCount)
                .map(feed -> FeedRes.builder()
                        .id(feed.getId())
                        .contents(feed.getContent())
                        .profileName(feed.getProfile().getProfileNickname())
                        .profileImageUrl(feed.getProfile().getProfileImageUrl())
                        .commentCount(feed.getCommentCount())
                        .hashTag(feed.getHashTags())
                        .likeCount(feed.getLikeCount())
                        .isLiked(false)
                        .createdTime(feed.getCreatedTime())
                        .modifiedTime(feed.getModifiedTime())
                        .build())
                .toList();
    }
}