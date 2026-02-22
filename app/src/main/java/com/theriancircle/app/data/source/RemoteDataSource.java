package com.theriancircle.app.data.source;

import com.theriancircle.app.data.model.CommunityGroup;
import com.theriancircle.app.data.model.Comment;
import com.theriancircle.app.data.model.Event;
import com.theriancircle.app.data.model.Message;
import com.theriancircle.app.data.model.Post;

import java.util.List;

public interface RemoteDataSource {
    List<Post> fetchFeedPosts();
    Post createPost(String authorName, String authorSpecies, String content);
    Post toggleLike(String postId);
    Comment addComment(String postId, String authorName, String text);
    List<CommunityGroup> fetchGroups();
    List<Event> fetchEvents();
    List<Message> fetchMessages();
    Message sendMessage(String authorName, String text);
}
