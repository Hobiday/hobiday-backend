package com.example.culture.domain.feed.dto;

import com.example.culture.domain.comment.entity.Comment;
import com.example.culture.domain.feed.entity.HashTag;
import com.example.culture.domain.feed.entity.Picture;
import com.example.culture.domain.like.entity.Like;
import com.example.culture.domain.profile.entity.Profile;
import lombok.Getter;

import java.util.List;

@Getter
public class FeedReq {
    private String content;
    private Profile profile;
    private Picture picture;
    private List<HashTag> hashTags;
    private List<Like> likeList;
    private List<Comment> commentList;
}
