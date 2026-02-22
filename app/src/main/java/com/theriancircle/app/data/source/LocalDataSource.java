package com.theriancircle.app.data.source;

import com.theriancircle.app.data.model.CommunityGroup;
import com.theriancircle.app.data.model.Comment;
import com.theriancircle.app.data.model.Event;
import com.theriancircle.app.data.model.Message;
import com.theriancircle.app.data.model.Post;

import java.util.List;

public interface LocalDataSource {
    List<Post> getFeedPosts();
    void saveFeedPosts(List<Post> posts);
    Post createPost(String authorName, String authorSpecies, String content);
    Post toggleLike(String postId);
    Comment addComment(String postId, String authorName, String text);

    List<CommunityGroup> getGroups();
    void saveGroups(List<CommunityGroup> groups);
    CommunityGroup toggleGroupMembership(String groupId);

    List<Event> getEvents();
    void saveEvents(List<Event> events);
    Event toggleEventAttendance(String eventId);

    List<Message> getMessages();
    void saveMessages(List<Message> messages);
    Message addMessage(String authorName, String text);
}
