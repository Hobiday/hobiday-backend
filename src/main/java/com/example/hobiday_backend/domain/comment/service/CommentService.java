package com.example.hobiday_backend.domain.comment.service;

import com.example.hobiday_backend.domain.comment.dto.CommentReq;
import com.example.hobiday_backend.domain.comment.dto.CommentRes;
import com.example.hobiday_backend.domain.comment.entity.Comment;
import com.example.hobiday_backend.domain.feed.entity.Feed;
import com.example.hobiday_backend.domain.feed.repository.FeedRepository;
import com.example.hobiday_backend.domain.profile.entity.Profile;
import com.example.hobiday_backend.domain.profile.repository.ProfileRepository;
import com.example.hobiday_backend.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final ProfileRepository profileRepository;

    public CommentRes createComment(Long feedId, CommentReq commentReq, User user, Long parentCommentId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("피드를 찾을 수 없습니다"));

        Profile profile = profileRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다"));

        Comment parentComment = null;
        if (parentCommentId != null) {
            parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new RuntimeException("부모 댓글이 존재하지 않아 대댓글을 작성할 수 없습니다"));
        }

        Comment comment = new Comment(commentReq, feed, profile, parentComment);
        Comment savedComment = commentRepository.save(comment);

        return CommentRes.from(savedComment, false, true);
    }

    public List<CommentRes> getCommentsByFeedId(Long feedId, User user) {
        List<Comment> comments = commentRepository.findAllByFeedIdAndParentCommentIsNull(feedId);

        return comments.stream()
                .map(comment -> {
                    boolean isLiked = comment.getLikeList().stream()
                            .anyMatch(like -> like.getUser().getUsername().equals(user.getUsername()));
                    boolean isAuthor = comment.getProfile().getUsername().equals(user.getUsername());
                    return CommentRes.from(comment, isLiked, isAuthor);
                })
                .collect(Collectors.toList());
    }

    public CommentRes updateComment(Long commentId, CommentReq commentReq, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getProfile().getUsername().equals(user.getUsername())) {
            throw new RuntimeException("Unauthorized");
        }

        comment.updateContents(commentReq.getContents());
        return CommentRes.from(commentRepository.save(comment), false, true);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getProfile().getUsername().equals(user.getUsername())) {
            throw new RuntimeException("Unauthorized");
        }

        commentRepository.deleteById(commentId);
    }
}
