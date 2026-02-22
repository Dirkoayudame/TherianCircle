package com.theriancircle.app.data.repository;

import com.theriancircle.app.data.model.Comment;
import com.theriancircle.app.data.model.Post;

import java.util.List;

public interface FeedRepository {
    List<Post> getFeedPosts();
    Post createPost(String authorName, String authorSpecies, String content);
    Post toggleLike(String postId);
    Comment addComment(String postId, String authorName, String text);
}
