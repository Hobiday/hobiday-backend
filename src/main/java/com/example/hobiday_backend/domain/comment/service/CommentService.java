package com.example.hobiday_backend.domain.comment.service;

import com.example.hobiday_backend.domain.comment.dto.CommentReq;
import com.example.hobiday_backend.domain.comment.dto.CommentRes;
import com.example.hobiday_backend.domain.comment.entity.Comment;
import com.example.hobiday_backend.domain.comment.exception.CommentErrorCode;
import com.example.hobiday_backend.domain.comment.exception.CommentException;
import com.example.hobiday_backend.domain.comment.repository.CommentRepository;
import com.example.hobiday_backend.domain.feed.entity.Feed;
import com.example.hobiday_backend.domain.feed.exception.FeedErrorCode;
import com.example.hobiday_backend.domain.feed.exception.FeedException;
import com.example.hobiday_backend.domain.feed.repository.FeedRepository;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.exception.ProfileErrorCode;
import com.example.hobiday_backend.domain.profile.exception.ProfileException;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final ProfileRepository profileRepository;

    public CommentRes createComment(Long feedId, CommentReq commentReq, Long userId) {
        if (commentReq.getContents() == null || commentReq.getContents().trim().isEmpty()) {
            throw new CommentException(CommentErrorCode.COMMENT_EMPTY_CONTENT);
        }
        // 피드 프로필 예외 추가 시켜줘야 함
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));
        Profile profile = profileRepository.findByMemberId(userId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));
        // 엔티티로 저장
        Comment comment = Comment.builder()
                .contents(commentReq.getContents())
                .feed(feed)
                .profile(profile)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return CommentRes.from(savedComment);
    }

    public List<CommentRes> getCommentsByFeedId(Long feedId) {
        List<Comment> comments = commentRepository.findAllByFeedIdOrderByCreatedTimeAsc(feedId);
        return comments.stream()
                .map(CommentRes::from)
                .collect(Collectors.toList());
    }

    public CommentRes updateComment(Long commentId, CommentReq commentReq, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        Profile profile = profileRepository.findByMemberId(userId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

        // 3. 댓글 작성자와 요청 사용자의 프로필 ID 비교
        if (!comment.getProfile().getId().equals(profile.getId())) {
            throw new CommentException(CommentErrorCode.COMMENT_UPDATE_ACCESS_DENIED);
        }

        comment.updateContents(commentReq.getContents());
        commentRepository.save(comment);
        return CommentRes.from(comment);
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        Profile profile = profileRepository.findByMemberId(userId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

        if (!comment.getProfile().getId().equals(profile.getId())) {
            throw new CommentException(CommentErrorCode.COMMENT_DELETE_ACCESS_DENIED);
        }
        commentRepository.delete(comment);
    }
}
