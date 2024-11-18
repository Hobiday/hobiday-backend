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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;

    // 댓글 작성
    @Transactional
    public CommentRes getComments(Long id, CommentReq commentReq) {
        // 댓글 등록
        Feed targetFeed = feedRepository.findById(id)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));
        Long parentCommentId = commentReq.getParentCommentId();
        // comment entity 생성
        Comment targetComment = Comment.builder()
                .feed(targetFeed)
                .contents(commentReq.getContents())
                .parentCommentId(commentReq.getParentCommentId())
                .build();
      /*  //피드에 댓글 추가// 이건 피드 서비스에서 처리하기
        targetComment.getFeed().getCommentList().add(targetComment);*/

        if (targetComment.getParentCommentId() == null) {
            commentRepository.save(targetComment);
        }
        // parentComment 가 있다면 parent comment 에 childComment 를 추가
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.PARENT_COMMENT_NOT_FOUND));
        parentComment.addChildComment(targetComment);
        commentRepository.save(targetComment);

        log.info(targetComment.toString());
        // dto로 반환 빌더 추가로 만들어주기
        return null; // 일단은 null
    }


    // 댓글 수정


    // 댓글 삭제

    // 댓글 조회


}
