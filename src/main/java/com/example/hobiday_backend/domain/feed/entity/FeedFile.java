package com.example.hobiday_backend.domain.feed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileUrl; // S3 파일 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed; // 피드와 연관 관계

    @Builder
    public FeedFile(String fileUrl, Feed feed) {
        String[] urls = fileUrl.split("/");
        urls[2] = "cdn.hobiday.site";
        String cdnUrl = "https://";
        for (int i = 2; i < urls.length; i++) {
            cdnUrl += urls[i] + "/";
        }
        cdnUrl = cdnUrl.substring(0, cdnUrl.length() - 1);
        this.fileUrl = cdnUrl;

        this.feed = feed;
    }
}

