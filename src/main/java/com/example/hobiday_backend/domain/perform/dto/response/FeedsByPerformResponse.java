package com.example.hobiday_backend.domain.perform.dto.response;

import com.example.hobiday_backend.domain.feed.entity.Feed;
import com.example.hobiday_backend.domain.feed.entity.FeedFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FeedsByPerformResponse {
    private Long feedId;
    private String contents;
    private List<String> feedFiles;

    // 공연상세에서 피드 클릭시 작성된 피드목록 조회
    public static FeedsByPerformResponse from(Feed feed) {
        return FeedsByPerformResponse.builder()
                .feedId(feed.getId())
                .contents(feed.getContent())
                .feedFiles(feed.getFeedFiles().stream()
                        .map(FeedFile::getFileUrl)
                        .collect(Collectors.toList()))
                .build();
    }
}
